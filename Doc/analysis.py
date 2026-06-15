import os

import matplotlib

matplotlib.use("Agg")
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd

DOC = "/Users/kamilbarszczak/Desktop/studies/Crowd_pressure/Doc"
DATA = f"{DOC}/sim_data"
DETAIL = f"{DATA}/detail"
FIG = f"{DOC}/figures"
os.makedirs(FIG, exist_ok=True)

BOARD_W, BOARD_H = 750, 450

WALLS = {
    "Map7": [
        (0, 213, 363, 213),
        (0, 237, 363, 237),
        (387, 213, 749, 213),
        (387, 237, 749, 237),
        (363, 0, 363, 213),
        (387, 0, 387, 213),
        (363, 237, 363, 449),
        (387, 237, 387, 449),
    ],
    "Map1": [
        (375, 449, 375, 227.5),
        (375, 0, 375, 221.5),
    ],
}

# Bottleneck measurement window for Map7 (around the central opening)
BOTTLENECK = dict(x0=325, x1=425, y0=200, y1=250)


def draw_walls(ax, map_name):
    for x0, y0, x1, y1 in WALLS.get(map_name, []):
        ax.plot([x0, x1], [y0, y1], color="#222222", lw=2, zorder=5)


def agg_summary():
    df = pd.read_csv(f"{DATA}/summary.csv")
    grp = df.groupby(["map", "scenario", "agent_count"])
    out = grp.agg(
        mean_pressure=("mean_pressure", "mean"),
        std_pressure=("mean_pressure", "std"),
        max_pressure=("max_pressure", "mean"),
        evac_fraction=("evac_fraction", "mean"),
        std_evac_fraction=("evac_fraction", "std"),
        mean_speed=("mean_speed", "mean"),
        std_speed=("mean_speed", "std"),
    ).reset_index()
    evac = (
        df[df.evac_time_s > 0]
        .groupby(["map", "scenario", "agent_count"])
        .agg(evac_time=("evac_time_s", "mean"), std_evac_time=("evac_time_s", "std"))
        .reset_index()
    )
    return df, out, evac


def fig_pressure_vs_n(out):
    fig, ax = plt.subplots(figsize=(7, 4.5))
    for (m, s), d in out.groupby(["map", "scenario"]):
        d = d.sort_values("agent_count")
        ax.errorbar(
            d.agent_count,
            d.mean_pressure,
            yerr=d.std_pressure.fillna(0),
            marker="o",
            capsize=3,
            label=f"{m} / {s}",
        )
    ax.set_xlabel("Number of agents")
    ax.set_ylabel("Mean crowd pressure [a.u.]")
    ax.set_title("Mean crowd pressure vs. crowd size")
    ax.legend()
    ax.grid(alpha=0.3)
    fig.tight_layout()
    fig.savefig(f"{FIG}/pressure_vs_n.png", dpi=140)
    plt.close(fig)


def fig_evac_fraction(out):
    fig, ax = plt.subplots(figsize=(7, 4.5))
    for (m, s), d in out.groupby(["map", "scenario"]):
        if s != "both":
            continue
        d = d.sort_values("agent_count")
        ax.errorbar(
            d.agent_count,
            d.evac_fraction * 100,
            yerr=d.std_evac_fraction.fillna(0) * 100,
            marker="s",
            capsize=3,
            label=f"{m} / {s}",
        )
    ax.set_xlabel("Number of agents")
    ax.set_ylabel("Evacuated within 125 s [%]")
    ax.set_title("Evacuation success vs. crowd size (with heuristics)")
    ax.set_ylim(0, 105)
    ax.legend()
    ax.grid(alpha=0.3)
    fig.tight_layout()
    fig.savefig(f"{FIG}/evac_fraction_vs_n.png", dpi=140)
    plt.close(fig)


def fig_evac_time(evac):
    d = evac[(evac["map"] == "Map7") & (evac.scenario == "both")].sort_values(
        "agent_count"
    )
    if d.empty:
        return
    fig, ax = plt.subplots(figsize=(7, 4.5))
    ax.errorbar(
        d.agent_count,
        d.evac_time,
        yerr=d.std_evac_time.fillna(0),
        marker="o",
        capsize=3,
        color="#b5651d",
    )
    ax.set_xlabel("Number of agents")
    ax.set_ylabel("Full-evacuation time [s]")
    ax.set_title("Map7 bottleneck: evacuation time vs. crowd size")
    ax.grid(alpha=0.3)
    fig.tight_layout()
    fig.savefig(f"{FIG}/evac_time_vs_n.png", dpi=140)
    plt.close(fig)


def fig_speed_vs_n(out):
    fig, ax = plt.subplots(figsize=(7, 4.5))
    for (m, s), d in out.groupby(["map", "scenario"]):
        d = d.sort_values("agent_count")
        ax.errorbar(
            d.agent_count,
            d.mean_speed,
            yerr=d.std_speed.fillna(0),
            marker="o",
            capsize=3,
            label=f"{m} / {s}",
        )
    ax.set_xlabel("Number of agents")
    ax.set_ylabel("Mean speed of moving agents [px/s]")
    ax.set_title("Mean speed vs. crowd size")
    ax.legend()
    ax.grid(alpha=0.3)
    fig.tight_layout()
    fig.savefig(f"{FIG}/speed_vs_n.png", dpi=140)
    plt.close(fig)


def load_detail(map_name):
    path = f"{DETAIL}/{map_name}_both_n150_rep0.csv"
    if not os.path.exists(path):
        return None
    return pd.read_csv(path)


def fig_trajectories(df, map_name):
    fig, ax = plt.subplots(figsize=(8, 5))
    groups = df.group.unique()
    palette = {
        g: c for g, c in zip(groups, ["#d62728", "#2ca02c", "#1f77b4", "#9467bd"])
    }
    for gid, sub in df.groupby("id"):
        ax.plot(
            sub.x,
            sub.y,
            color=palette.get(sub.group.iloc[0], "#888"),
            lw=0.5,
            alpha=0.5,
        )
    draw_walls(ax, map_name)
    ax.set_xlim(0, BOARD_W)
    ax.set_ylim(BOARD_H, 0)
    ax.set_title(f"{map_name}: agent trajectories (N=150, both heuristics)")
    ax.set_xlabel("x [px]")
    ax.set_ylabel("y [px]")
    fig.tight_layout()
    fig.savefig(f"{FIG}/{map_name}_trajectories.png", dpi=140)
    plt.close(fig)


def fig_heatmap(df, map_name, weight, fname, title, cmap):
    w = df[weight].values if weight else None
    fig, ax = plt.subplots(figsize=(8, 5))
    h, xe, ye = np.histogram2d(
        df.x, df.y, bins=[75, 45], range=[[0, BOARD_W], [0, BOARD_H]], weights=w
    )
    if weight:
        counts, _, _ = np.histogram2d(
            df.x, df.y, bins=[75, 45], range=[[0, BOARD_W], [0, BOARD_H]]
        )
        h = np.divide(h, counts, out=np.zeros_like(h), where=counts > 0)
    im = ax.imshow(
        h.T, origin="upper", extent=[0, BOARD_W, BOARD_H, 0], aspect="auto", cmap=cmap
    )
    draw_walls(ax, map_name)
    fig.colorbar(im, ax=ax, label=("mean pressure [a.u.]" if weight else "visits"))
    ax.set_title(title)
    ax.set_xlabel("x [px]")
    ax.set_ylabel("y [px]")
    fig.tight_layout()
    fig.savefig(f"{FIG}/{fname}.png", dpi=140)
    plt.close(fig)


def fig_timeseries(df, map_name):
    active = df[df.stopped == 0]
    g = active.groupby("t")
    ts = g.agg(
        n_active=("id", "count"),
        mean_pressure=("pressure", "mean"),
        mean_speed=("speed", "mean"),
    ).reset_index()
    fig, axs = plt.subplots(3, 1, figsize=(8, 7), sharex=True)
    axs[0].plot(ts.t, ts.n_active, color="#1f77b4")
    axs[0].set_ylabel("active agents")
    axs[1].plot(ts.t, ts.mean_pressure, color="#d62728")
    axs[1].set_ylabel("mean pressure")
    axs[2].plot(ts.t, ts.mean_speed, color="#2ca02c")
    axs[2].set_ylabel("mean speed [px/s]")
    axs[2].set_xlabel("time [s]")
    axs[0].set_title(f"{map_name}: time series (N=150, both heuristics)")
    for a in axs:
        a.grid(alpha=0.3)
    fig.tight_layout()
    fig.savefig(f"{FIG}/{map_name}_timeseries.png", dpi=140)
    plt.close(fig)


def fig_fundamental(df, map_name):
    b = BOTTLENECK
    region = df[
        (df.x >= b["x0"])
        & (df.x <= b["x1"])
        & (df.y >= b["y0"])
        & (df.y <= b["y1"])
        & (df.stopped == 0)
    ]
    if region.empty:
        return
    area = (b["x1"] - b["x0"]) * (b["y1"] - b["y0"]) / 1e4  # per 100x100 px cell
    g = region.groupby("t")
    fd = g.agg(count=("id", "count"), mean_speed=("speed", "mean")).reset_index()
    fd["density"] = fd["count"] / area
    fd["flow"] = fd["density"] * fd["mean_speed"]
    fig, axs = plt.subplots(1, 2, figsize=(11, 4.5))
    axs[0].scatter(fd.density, fd.mean_speed, s=8, alpha=0.4, color="#1f77b4")
    axs[0].set_xlabel("local density [agents / 100x100 px]")
    axs[0].set_ylabel("mean speed [px/s]")
    axs[0].set_title("Speed-density relation")
    axs[1].scatter(fd.density, fd.flow, s=8, alpha=0.4, color="#d62728")
    axs[1].set_xlabel("local density [agents / 100x100 px]")
    axs[1].set_ylabel("flow [density x speed]")
    axs[1].set_title("Fundamental diagram")
    for a in axs:
        a.grid(alpha=0.3)
    fig.suptitle(f"{map_name} bottleneck (N=150, both heuristics)")
    fig.tight_layout()
    fig.savefig(f"{FIG}/{map_name}_fundamental_diagram.png", dpi=140)
    plt.close(fig)


def main():
    df, out, evac = agg_summary()
    fig_pressure_vs_n(out)
    fig_evac_fraction(out)
    fig_evac_time(evac)
    fig_speed_vs_n(out)

    for map_name in ["Map7", "Map1"]:
        detail = load_detail(map_name)
        if detail is None:
            continue
        fig_trajectories(detail, map_name)
        fig_heatmap(
            detail,
            map_name,
            None,
            f"{map_name}_density_heatmap",
            f"{map_name}: occupancy density (N=150)",
            "viridis",
        )
        fig_heatmap(
            detail,
            map_name,
            "pressure",
            f"{map_name}_pressure_heatmap",
            f"{map_name}: mean crowd pressure (N=150)",
            "inferno",
        )
        fig_timeseries(detail, map_name)
        fig_fundamental(detail, map_name)

    print("Figures written to", FIG)
    print(out.to_string(index=False))


if __name__ == "__main__":
    main()

![banner](https://user-images.githubusercontent.com/72699445/231688107-9d10af92-ed74-4461-bd5e-1902ec621f10.png)

This project is the simulation of a crowd passing the narrow way. The implementation is based on [Social Force Model](https://www.researchgate.net/publication/1947096_Social_Force_Model_for_Pedestrian_Dynamics) presented in 1998. The model is modified by implementing heuristics that modify the behavior of each agent. The following screenshots present the application layout.

The configuration window:

<img width="750" alt="window_configuration" src="https://user-images.githubusercontent.com/72699445/231693040-9647d912-53b4-4a9b-87f6-66d6569395b2.png">

The simulation window:

<img width="750" alt="window_simulation" src="https://user-images.githubusercontent.com/72699445/231693182-0e1c556f-475b-4c21-ab88-abe1eee03cfb.png">

## Motivation

This project is made to test crowd behavior in stressful situations such as evacuations, fires, or floods.

## Build Status

The application is almost completely finished. There are written tests for each math function and the whole system is well-tested so there is a low probability that any bugs still exist. The only thing that may be improved/implemented is:
- the application visualizer class which may change the agent color accordingly to the pressure put on one (it is marked by the proper todo in the code)

## Tech/Framework used

This project uses:
- Java 17
- JavaFX 19.0.2.1
- JUnit 5.9.2

The implemented model is based on the following articles:
- [How simple rules determine pedestrian behavior and crowd disasters](https://www.pnas.org/doi/10.1073/pnas.1016507108)
- [Understanding Social-Force Model in Psychological Principles of Collective Behavior](https://www.researchgate.net/publication/303302099_Understanding_Social-Force_Model_in_Psychological_Principles_of_Collective_Behavior)
- [Social Force Model for Pedestrian Dynamics](https://www.researchgate.net/publication/1947096_Social_Force_Model_for_Pedestrian_Dynamics)
- [Deep Social Force](https://www.researchgate.net/publication/354857367_Deep_Social_Force)

## Screenshots

The simulation work is shown below screenshots

1st situation:

<img width="750" alt="sim_1_1" src="https://user-images.githubusercontent.com/72699445/231701257-915101be-87cd-4f0c-8592-d9040ab66eda.png">

<img width="750" alt="sim_1_4" src="https://user-images.githubusercontent.com/72699445/231701297-1e6cd61b-8da5-43c4-9964-fd9ef2a34289.png">

2nd situation:

<img width="750" alt="sim_2_1" src="https://user-images.githubusercontent.com/72699445/231701364-65d05388-bf35-4248-b1fc-36f128b3b177.png">

<img width="750" alt="sim_2_3" src="https://user-images.githubusercontent.com/72699445/231701415-88b2be6e-8086-48a6-a757-07aae91f98d9.png">

## Features

The application architecture was designed to be as flexible as possible. Its design allows the user to turn off and on the heuristics and to change the calculation engine during the work. Possible calculation engines are SingleThread and MultiThreadPoolBased. Moreover, the design allows to implementation of other custom heuristics or calculation engines without modifying the application core. To sum up, the key features of this project are:
- the ease of modifying the heuristics and calculation engine during the work
- the ease of adding/modifying new heuristics or calculation engines
- the physical model may also be easily changed
- the application map and agents placement may be changed by implementing the proper initializer interfaces

All of that is to achived by the design presented on the below simplified UML diagram:

<img width="750" alt="desing_uml" src="https://user-images.githubusercontent.com/72699445/231698047-95269729-ff12-4e5a-9d38-865f8dde7394.jpg">

## Installation

To install the application please follow the below steps:
1. Clone the repository
```
mkdir crowd-pressure
cd crowd-pressure
git clone https://github.com/kbarszczak/Crowd_pressure .
```
2. Install all dependencies
```
cd Crowd_pressure
mvn clean install
```
3. Run the jar file
```
java -jar target/CrowdPressureSimulation.jar
```

Since now the application should launch and the configuration window should be displayed.

## How to Use?

Before the installation, one may implement custom initializers for both the map and the agents to run the simulation in the tested environment. If you are not interested in implementing custom initializes you may use those already implemented. Once the application is launched set up the following:
- the agent count (this is the number of all the agents present in the simulation)
- scale coefficient (this is the coefficient applied to the physical calculation. The bigger the number the more the physical impact is)
- destination radius (this is the radius of the circle that the agents are trying to reach)
- delay in ms (since the simulation world is discrete the ticking time has to be specified. The lower the number the more precise the simulation is)
- computation engine (it specifies whether the simulation runs on the single-thread or multi-thread engine)
- map (this changes the simulation environment)

After the setup process, run the simulation by pushing the start button.

## Contribute
- clone the repository
- either make the changes or implement todos
- create the pull request with a detailed description of your changes

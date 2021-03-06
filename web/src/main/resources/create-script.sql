CREATE TABLE javascript_users (username VARCHAR(20) NOT NULL, password VARCHAR(255) NOT NULL, PRIMARY KEY (username));
CREATE TABLE javascript_groups (username VARCHAR(20) NOT NULL, group_name VARCHAR(20) NOT NULL, PRIMARY KEY (username, group_name));
ALTER TABLE javascript_groups ADD CONSTRAINT fk_username FOREIGN KEY(username) REFERENCES javascript_users (username);



--CREATE TABLE EXPERIMENT (EXPERIMENT_ID INTEGER, EXPERIMENT_NAME VARCHAR(50), PRIMARY KEY (EXPERIMENT_ID));

--CREATE TABLE PARAMETER (PARAMETER_ID INTEGER, PARAMETER_NAME VARCHAR(50), PRIMARY KEY (PARAMETER_ID));

--CREATE TABLE EXPERIMENT_PARAMETERS (EXPERIMENT_PARAMETERS_ID INTEGER, EXPERIMENT_ID INTEGER, PARAMETER_ID INTEGER, PARAMETER_VALUE FLOAT, PRIMARY KEY (EXPERIMENT_PARAMETERS_ID), FOREIGN KEY (EXPERIMENT_ID ) REFERENCES EXPERIMENT(EXPERIMENT_ID), FOREIGN KEY (PARAMETER_ID ) REFERENCES PARAMETER(PARAMETER_ID));

--CREATE TABLE SIMULATION (SIMULATION_ID INTEGER, EXPERIMENT_ID INTEGER,  RESULT FLOAT, PRIMARY KEY (SIMULATION_ID), FOREIGN KEY (EXPERIMENT_ID ) REFERENCES EXPERIMENT(EXPERIMENT_ID));

-- CREATE TABLE SIMULATION_ITERATION (SIM_ITERATION_ID INTEGER, SIMULATION_ID INTEGER, PARTIAL_RESULT FLOAT,  VARIABLES_VALUES VARCHAR(100), PRIMARY KEY (SIM_ITERATION_ID), FOREIGN KEY (SIMULATION_ID) REFERENCES SIMULATION (SIMULATION_ID));

-- CREATE TABLE SOLUTION (SOLUTION_ID INTEGER, SIM_ITERATION_ID INTEGER, PRIMARY KEY (SOLUTION_ID), FOREIGN KEY (SIM_ITERATION_ID) REFERENCES SIMULATION_ITERATION(SIM_ITERATION_ID));

CREATE TABLE SOLUTION_VARIABLE ( SOLUTION_VARIABLE_ID INTEGER, SOLUTION_ID INTEGER, VARIABLE_VALUE FLOAT, PRIMARY KEY (SOLUTION_VARIABLE_ID), FOREIGN KEY (SOLUTION_ID) REFERENCES SOLUTION(SOLUTION_ID));

CREATE TABLE NETWORK (NETWORK_ID INTEGER, SOLUTION_ID INTEGER, PRIMARY KEY (NETWORK_ID), FOREIGN KEY (SOLUTION_ID) REFERENCES SOLUTION(SOLUTION_ID));

CREATE TABLE NETWORK_NODE (NODE_ID INTEGER, NETWORK_ID INTEGER, PRIMARY KEY (NODE_ID), FOREIGN KEY (NETWORK_ID) REFERENCES NETWORK(NETWORK_ID));

CREATE TABLE NODE_CONNECTION (NODE_CONNECTION_ID INTEGER, FROM_NODE_ID INTEGER, TO_NODE_ID INTEGER, PRIMARY KEY (NODE_CONNECTION_ID), FOREIGN KEY (FROM_NODE_ID) REFERENCES NETWORK_NODE(NODE_ID), FOREIGN KEY (TO_NODE_ID) REFERENCES NETWORK_NODE(NODE_ID));


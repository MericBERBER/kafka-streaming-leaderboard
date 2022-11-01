Topologies:

   Sub-topology: 0

    Source: KSTREAM-SOURCE-0000000000 (topics: [score-events])
      --> KSTREAM-KEY-SELECT-0000000001
    Processor: KSTREAM-KEY-SELECT-0000000001 (stores: [])
      --> KSTREAM-FILTER-0000000009
      <-- KSTREAM-SOURCE-0000000000
    Processor: KSTREAM-FILTER-0000000009 (stores: [])
      --> KSTREAM-SINK-0000000008
      <-- KSTREAM-KEY-SELECT-0000000001
    Sink: KSTREAM-SINK-0000000008 (topic: KSTREAM-KEY-SELECT-0000000001-repartition)
      <-- KSTREAM-FILTER-0000000009

  Sub-topology: 1

    Source: KSTREAM-SOURCE-0000000010 (topics: [KSTREAM-KEY-SELECT-0000000001-repartition])
      --> KSTREAM-JOIN-0000000011
    Processor: KSTREAM-JOIN-0000000011 (stores: [players-STATE-STORE-0000000002])
      --> KSTREAM-LEFTJOIN-0000000012
      <-- KSTREAM-SOURCE-0000000010
    Processor: KSTREAM-LEFTJOIN-0000000012 (stores: [])
      --> KSTREAM-KEY-SELECT-0000000014, KSTREAM-PRINTER-0000000013
      <-- KSTREAM-JOIN-0000000011
    Processor: KSTREAM-KEY-SELECT-0000000014 (stores: [])
      --> leader-boards-repartition-filter
      <-- KSTREAM-LEFTJOIN-0000000012
    Source: KSTREAM-SOURCE-0000000003 (topics: [players])
      --> KTABLE-SOURCE-0000000004
    Processor: leader-boards-repartition-filter (stores: [])
      --> leader-boards-repartition-sink
      <-- KSTREAM-KEY-SELECT-0000000014
    Processor: KSTREAM-PRINTER-0000000013 (stores: [])
      --> none
      <-- KSTREAM-LEFTJOIN-0000000012
    Processor: KTABLE-SOURCE-0000000004 (stores: [players-STATE-STORE-0000000002])
      --> none
      <-- KSTREAM-SOURCE-0000000003
    Sink: leader-boards-repartition-sink (topic: leader-boards-repartition)
      <-- leader-boards-repartition-filter

  Sub-topology: 2 for global store (will not generate tasks)

    Source: KSTREAM-SOURCE-0000000006 (topics: [games])
      --> KTABLE-SOURCE-0000000007
    Processor: KTABLE-SOURCE-0000000007 (stores: [games-STATE-STORE-0000000005])
      --> none
      <-- KSTREAM-SOURCE-0000000006
  Sub-topology: 3

    Source: leader-boards-repartition-source (topics: [leader-boards-repartition])
      --> KSTREAM-AGGREGATE-0000000015
    Processor: KSTREAM-AGGREGATE-0000000015 (stores: [leader-boards])
      --> KTABLE-TOSTREAM-0000000019
      <-- leader-boards-repartition-source
    Processor: KTABLE-TOSTREAM-0000000019 (stores: [])
      --> KSTREAM-SINK-0000000020
      <-- KSTREAM-AGGREGATE-0000000015
    Sink: KSTREAM-SINK-0000000020 (topic: high-scores)
      <-- KTABLE-TOSTREAM-0000000019

![image](https://user-images.githubusercontent.com/20739328/199293578-aaf5f98b-937d-446a-a573-d54fcab44212.png)

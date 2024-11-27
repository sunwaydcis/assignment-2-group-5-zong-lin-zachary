# Hospital Data Analysis Project
Contributors: Zachary Soo Lap Vai, Goo Zong Lin

## Project Overview
Analysis of hospital data focusing on bed capacity and COVID-19 metrics across different states. The project processes CSV data to answer key questions about hospital resource allocation and patient admissions.

## Questions
1. Which state has the highest total hospital bed?
2. What are the ratio of bed dedicated for COVID-19 to total of available hospital bed in the dataset?
3. What are the averages of individuals in category x where x can be suspected/probable, COVID-19 positive, or non-COVID is being admitted to hospitals for each state?


## Technical Implementation

### Data Model
```scala
case class HospitalData(
  // Add relevant fields here
)
```

### Core Technologies
- **Scala Collections Framework**
    - List operations for data processing
    - Map structures for state/date-based aggregations
    - Stream processing for efficient data handling

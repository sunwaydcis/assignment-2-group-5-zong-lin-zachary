import scala.io.Source // Read the CSV FIle

// Case class is used to represent the data structure of the CSV file.
case class HospitalData(
                         date: String,
                         state: String,
                         beds: Int,
                         beds_covid: Int,
                         beds_noncrit: Int,
                         admitted_pui: Int,
                         admitted_covid: Int,
                         admitted_total: Int,
                         discharged_pui: Int,
                         discharged_covid: Int,
                         discharged_total: Int,
                         hosp_covid: Int,
                         hosp_pui: Int,
                         hosp_noncovid: Int
)
/*
The map function is used to transform each line of the CSV file into HospitalData object.
*/
@main def HospitalAnalysis =
  // Read and Parse CSV data
  def readHospitalData(filePath: String): List[HospitalData] =
    val source = Source.fromFile(filePath)
    val lines = source.getLines().drop(1).toList

    lines.map { line =>
      val cols = line.split(",")
      HospitalData(
        cols(0),
        cols(1),
        cols(2).toInt,
        cols(3).toInt,
        cols(4).toInt,
        cols(5).toInt,
        cols(6).toInt,
        cols(7).toInt,
        cols(8).toInt,
        cols(9).toInt,
        cols(10).toInt,
        cols(11).toInt,
        cols(12).toInt,
        cols(13).toInt
      )
    }

  // Load Data with validation
  val data = try
    val loadedData = readHospitalData("hospital.csv")
    println("Data loaded successfully.")
    loadedData
  catch
    case e: java.io.FileNotFoundException =>
      println("File not found.")
      System.exit(1)
      List.empty[HospitalData]

  // Question 1: State with the highest total hospital beds
  def calculateStateWithMaxBeds(states: List[HospitalData]): Unit = {
    // Group the records by state and date
    val stateWithMaxBeds = states
      .groupBy(record => (record.state, record.date))
      // For each group, find the maximum number of beds
      .map { case ((state, date), records) =>
        (state, date, records.map(_.beds).max)
      }
      // Convert the map to a list
      .toList
      // Find the entry with the maximum number of beds
      .maxBy(_._3)

    println("\nState with the highest number of beds on any date:")
    println(s"Answer: ${stateWithMaxBeds._1} on ${stateWithMaxBeds._2} with ${stateWithMaxBeds._3} hospital beds")
    println("-" * 70) // Separator
    println() // New line
  }
  
  // Question 2: What are the ratio of bed dedicated for COVID-19 to total of available hospital bed in the dataset?
  def calculateCovidBedRatioForMalaysia(states: List[HospitalData]): Unit = {
    // Calculate the total number of beds dedicated for COVID - 19, using SUM function
    val totalBedsCovid = states.map(_.beds_covid).sum
    // Calculate the total number of beds available in the dataset, using SUM Function
    val totalBeds = states.map(_.beds).sum
    // Calculate the ratio of beds dedicated for COVID - 19 to total available hospital beds
    val ratioCovidBeds = totalBedsCovid.toDouble / totalBeds
    // Round the ratio to two decimal places
    val roundedRatioCovidBeds = (ratioCovidBeds * 100).round / 100.0

    println(s"Ratio of beds dedicated for COVID-19 to total available hospital beds in Malaysia: $roundedRatioCovidBeds")
    println("-" * 100) // Separator
    println() // New line
  }
  
  // Question 3: What are the averages of individuals in category x where x can be suspected/probable, COVID-19 positive, or non-COVID is being admitted to hospitals for each state?
  def calculateAverageAdmittedPatientsByState(states: List[HospitalData]): Unit = {
    // Group the records by state
    val statesGrouped = states.groupBy(_.state)
    // Iterate over each group of records
    statesGrouped.foreach { case (stateKey, stateList) =>
      // Calculate the number of records for each state
      val numStates = stateList.length
      // Calculate the total number of COVID-19 admitted patients for the current state
      val covidAdmitted = stateList.map(_.admitted_covid).sum
      // Calculate the average number of COVID-19 admitted patients for the current state
      val avgCovidAdmitted = (covidAdmitted.toDouble / numStates).round
      // Calculate the total number of non-COVID admitted patients for the current state
      val nonCovidAdmitted = stateList.map(record => record.admitted_total - record.admitted_covid).sum
      // Calculate the average number of non-COVID admitted patients for the current state
      val avgNonCovidAdmitted = (nonCovidAdmitted.toDouble / numStates).round

      println(s"State: $stateKey, Average COVID-19 Admitted: $avgCovidAdmitted, Average Non-COVID Admitted: $avgNonCovidAdmitted")
      println("-" * 100) // Separator
      println() // New Line
    }
  }

  // Question 1
  calculateStateWithMaxBeds(data)
  // Question 2
  calculateCovidBedRatioForMalaysia(data)
  // Question 3
  calculateAverageAdmittedPatientsByState(data)
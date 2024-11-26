import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scala.io.Source
import scala.collection.mutable.Map

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

@main def HospitalAnalysis =
  //Read and Parse CSV data
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
  val stateWithMaxBeds = data
    .groupBy(record => (record.state, record.date))
    .map { case ((state, date), records) =>
      (state, date, records.map(_.beds).max)
    }
    .toList
    .maxBy(_._3)

  println("\nState with the highest number of beds on any date:")
  println(s"Answer: ${stateWithMaxBeds._1} on ${stateWithMaxBeds._2} with ${stateWithMaxBeds._3} hospital beds")

  // Question 2: What are the ratio of bed dedicated for COVID-19 to total of available hospital bed in the dataset?
  val totalBedsCovid = data.map(_.beds_covid).sum
  val totalBeds = data.map(_.beds).sum
  val ratioCovidBeds = totalBedsCovid.toDouble / totalBeds

  println("\nRatio of beds dedicated for COVID-19 to total available hospital beds:")
  println(f"Answer: $ratioCovidBeds%.2f")

  // Question 3: What are the averages of individuals in category x where x can be suspected/probable, COVID-19 positive, or non-COVID is being admitted to hospitals for each state?
  val averagesByState = data
    .groupBy(_.state)
    .map { case (state, records) =>
      val totalRecords = records.size
      val avgTotalAdmitted = (records.map(_.admitted_total).sum + totalRecords / 2) / totalRecords
      val avgCovidAdmitted = (records.map(_.admitted_covid).sum + totalRecords / 2) / totalRecords
      val avgNonCovidAdmitted = (records.map(record => record.admitted_total - record.admitted_covid).sum + totalRecords / 2) / totalRecords
      (state, avgTotalAdmitted, avgCovidAdmitted, avgNonCovidAdmitted)
    }

  println("\nAverages of admitted patients in each category for each state:")
  averagesByState.foreach { case (state, avgTotalAdmitted, avgCovidAdmitted, avgNonCovidAdmitted) =>
    println(f"State: $state, Total Admitted: $avgTotalAdmitted, COVID-19 Admitted: $avgCovidAdmitted, Non-COVID Admitted: $avgNonCovidAdmitted")
  }
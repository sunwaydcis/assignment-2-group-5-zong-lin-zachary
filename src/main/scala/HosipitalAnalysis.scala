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
    .groupBy(_.state)
    .map { case (state, records) =>
      (state, records.map(_.beds).sum)
    }
    .maxBy(_._2)

  println("\nQuestion 1: Which state has the highest total hospital beds?")
  println(s"Answer: ${stateWithMaxBeds._1} has the highest total with ${stateWithMaxBeds._2} hospital beds") 
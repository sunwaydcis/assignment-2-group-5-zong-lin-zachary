import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scala.io.Source
import scala.collection.mutable.mutable.Map

// Load Data with validation 
val data = try {
  val loadedData = readHospitalData("hosiptal.csv")
  println("Data loaded successfully.")
  loadedData
} catch {
  case e: java.io.FileInputStreamException =>
    println("File not found.")
    System.exit(1)
    List.empty
}

object MyApp extends JFXApp3:

  override def start(): Unit =
    stage = new PrimaryStage()

end MyApp

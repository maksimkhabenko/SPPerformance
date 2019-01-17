
import java.io.FileInputStream
import java.util.Properties

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class Elastic extends Simulation {

	val (elastichost, rps, duration) =
		try
		{
			val prop = new Properties()
			prop.load(new FileInputStream("src\\main\\resources\\configuration.properties"))
			(
		    prop.getProperty("elastichost"),
				new Integer(prop.getProperty("rps")),
				new Integer(prop.getProperty("duration"))
			)
		} catch { case e: Exception =>
			e.printStackTrace()
			sys.exit(1)
		}

	//val feeder = csv("src\\main\\resources\\data.csv").random

	val httpProtocol = http
		.baseUrl(elastichost)
		.inferHtmlResources()
		.acceptHeader("*/*")


	val scn = scenario("Elastic")
		.exec(http("Request")
			.get("/client_profiles/_search?terminate_after=10&q=John"))


	setUp(scn.inject(constantUsersPerSec(rps.toDouble) during (duration seconds))).protocols(httpProtocol)

}



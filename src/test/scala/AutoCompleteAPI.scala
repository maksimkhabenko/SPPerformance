
import java.io.FileInputStream
import java.util.Properties

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class AutoCompleteAPI extends Simulation {

	val (apihost,rps, duration) =
		try
		{
			val prop = new Properties()
			prop.load(new FileInputStream("src\\main\\resources\\configuration.properties"))
			(
				prop.getProperty("apihost"),
			new Integer(prop.getProperty("rps")),
			new Integer(prop.getProperty("duration"))
				)
		} catch { case e: Exception =>
			e.printStackTrace()
			sys.exit(1)
		}

	val httpProtocol = http
		.baseUrl(apihost)
		.inferHtmlResources()
		.acceptHeader("*/*")

	val scn = scenario("AutoComplete")
		.exec(http("John")
			.get("/api/v2/AutoComplete?p=1&s=10&q=john"))

		setUp(scn.inject(constantUsersPerSec(rps.toDouble) during (duration seconds))).protocols(httpProtocol)
}
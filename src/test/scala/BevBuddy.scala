
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class BevBuddy extends Simulation {

	val baseUrl: String = System.getProperty("gatling.baseUrl", "http://localhost:8080")

	val httpProtocol = http
		.baseUrl(baseUrl)
		.acceptHeader("*/*")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-US,en;q=0.5")
		.userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:61.0) Gecko/20100101 Firefox/61.0")

    val xsrfTokenExtract = regex("""Vaadin-Security-Key":\s?"([^"]*)""").saveAs("seckey")

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
		"origin" -> "http://localhost:8080")

	val headers_69 = Map(
		"content-type" -> "application/json; charset=UTF-8",
		"origin" -> "http://localhost:8080")

val initSyncAndClientIds = exec((session) => {
		session.setAll(
			"syncId" -> 0,
			"clientId" -> 0
		)
	})

	val url = "/"
	val uidlUrl = url + "?v-r=uidl&v-uiId=${uiId}"
	val uIdExtract = regex(""""v-uiId":(\d+)""").saveAs("uiId")
  val syncIdExtract = regex("""syncId":([0-9]*)""").saveAs("syncId")
	val clientIdExtract = regex("""clientId":([0-9]*)""").saveAs("clientId")
	val gridIdExtract        = regex("""node":(\d+),"type":"put","key":"tag","feat":[0-9],"value":"vaadin-grid"""").saveAs("gridId")
	val searchFieldIdExtract = regex("""node":(\d+),"type":"splice","feat":[0-9]*,"index":[0-9]*,"add":\["view-toolbar__search-field"""").saveAs("searchFieldId")

	val chain_0 = exec(http("request_0")
			 .get(url)
			 .headers(headers_0)
		   .check(uIdExtract)
			 .check(xsrfTokenExtract)
			)
			.exec(initSyncAndClientIds)
			.pause(2)
			
		.exec(http("Navigate to Categories")
			.post(uidlUrl)
			.headers(headers_69)
			.body(ElFileBody("BevBuddy_0069_request.txt"))
			.check(regex("""Categories"""))
			.check(gridIdExtract)
			.check(searchFieldIdExtract)
			.check(syncIdExtract).check(clientIdExtract)
		)
		.pause(2)
		.exec(http("Load view")
			.post(uidlUrl)
			.headers(headers_69)
			.body(ElFileBody("BevBuddy_0096_request.txt"))
			.check(syncIdExtract).check(clientIdExtract)
			)
		.pause(3)
		.exec(http("Navigate to Reviews")
			.post(uidlUrl)
			.headers(headers_69)
			.body(ElFileBody("BevBuddy_0097_request.txt"))
		  .check(regex("""Reviews"""))
		  .check(syncIdExtract).check(clientIdExtract)
		)
		.pause(3)
		.exec(http("Navigate again to Categories")
			.post(uidlUrl)
			.headers(headers_69)
			.body(ElFileBody("BevBuddy_0069_request.txt"))
			.check(regex("""Categories"""))
			.check(gridIdExtract)
			.check(searchFieldIdExtract)
			.check(syncIdExtract).check(clientIdExtract)
		)
		.pause(2)
	  	.exec(http("Load view")
			.post(uidlUrl)
			.headers(headers_69)
			.body(ElFileBody("BevBuddy_0096_request.txt"))
			.check(syncIdExtract).check(clientIdExtract)
			)
		.pause(3)
		 .exec(http("Navigate again to Reviews")
			 .post(uidlUrl)
			 .headers(headers_69)
			 .body(ElFileBody("BevBuddy_0097_request.txt"))
			 .check(regex("""Reviews"""))
			 .check(syncIdExtract).check(clientIdExtract)
		 )
		
					
	val scn = scenario("BevBuddy").repeat(1) {exec(chain_0)}

	setUp(scn.inject(rampUsers(1) during 1)).protocols(httpProtocol)
}
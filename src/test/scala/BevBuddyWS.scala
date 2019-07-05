import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import io.gatling.core.session._
import io.gatling.core.session.el._

class BevBuddyWS extends Simulation {

  val baseUrl: String = System.getProperty("gatling.baseUrl", "http://localhost:8080")

  val httpProtocol = http
    .baseUrl(baseUrl)
    .acceptHeader("*/*")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:61.0) Gecko/20100101 Firefox/61.0")
    .wsBaseUrl("ws://localhost:8080")

  val xsrfTokenExtract = regex("""Vaadin-Security-Key":\s?"([^"]*)""").saveAs("seckey")
  val pushIdExtract = regex("""Vaadin-Push-ID":\s?"([^"]*)""").saveAs("pushId")

  val headers_0 = Map(
    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
    "origin" -> "http://localhost:8080")

  val headers_1 = Map(
    "Origin" -> baseUrl,
    "Host" -> "http://localhost:8080")

  val initSyncAndClientIds = exec((session) => {
    session.setAll(
      "syncId" -> 0,
      "clientId" -> 0
    )
  })

  val url = "/vaadinServlet/"
  val uIdExtract = regex(""""v-uiId":(\d+)""").saveAs("uiId")

  val atmoKeyCheck = ws.checkTextMessage("atmoKey").check(regex("[0-9]*\\|([^\\|]+).*").saveAs("atmokey"))

  val syncIdExtract = regex("""syncId":([0-9]*)""").saveAs("syncId")
  val clientIdExtract = regex("""clientId":([0-9]*)""").saveAs("clientId")
  val gridIdExtract = regex("""node":(\d+),"type":"put","key":"tag","feat":[0-9],"value":"vaadin-grid"""").saveAs("gridId")
  val searchFieldIdExtract = regex("""node":(\d+),"type":"splice","feat":[0-9]*,"index":[0-9]*,"add":\["view-toolbar__search-field"""").saveAs("searchFieldId")

  var rpcPrefix = """{"csrfToken":"${seckey}","rpc":["""
  var rpcSuffix = """],"syncId":${syncId},"clientId":${clientId}}"""

  def createMessage(msg: String): String = {
    rpcPrefix + msg + rpcSuffix
  }

  def atmoMessage(message: Expression[String]) = message.map(m => m.length + "|" + m)

  val chain_0 = exec(http("request_0")
    .get(url)
    .headers(headers_0)
    .check(uIdExtract)
    .check(xsrfTokenExtract)
    .check(pushIdExtract)
    )
    .exec(initSyncAndClientIds)
    .pause(2)
    .exec(
      ws("PUSH CHANNEL").connect(url+"?v-r=push&v-uiId=${uiId}&v-pushId=${pushId}&X-Atmosphere-tracking-id=0&X-Atmosphere-Framework=2.3.2.vaadin1-javascript&X-Atmosphere-Transport=websocket&X-Atmosphere-TrackMessageSize=true&Content-Type=application/json;%20charset=UTF-8&X-atmo-protocol=true HTTP/1.1")
        .await(30 seconds)(atmoKeyCheck)
    )
    .pause(2)
    .exec(
      ws("Navigate to Categories")
        .sendText(atmoMessage(createMessage("""{"type":"navigation","location":"categories","link":1}""")))
        .await(30 seconds)(
          ws.checkTextMessage("Categories view's checks")
            .check(gridIdExtract, searchFieldIdExtract, syncIdExtract, clientIdExtract)
        )
    )
    .pause(1)
    .exec(
      ws("Load view")
        .sendText(atmoMessage(createMessage("""{"type":"mSync","node":${searchFieldId},"feature":1,"property":"invalid","value":false},{"type":"publishedEventHandler","node":${gridId},"templateEventMethodName":"setDetailsVisible","templateEventMethodArgs":[null]},{"type":"publishedEventHandler","node":${gridId},"templateEventMethodName":"sortersChanged","templateEventMethodArgs":[[]]},{"type":"publishedEventHandler","node":${gridId},"templateEventMethodName":"confirmUpdate","templateEventMethodArgs":[0]}""")))
        .await(30 seconds)(
          ws.checkTextMessage("Sync and client id checks")
            .check(syncIdExtract, clientIdExtract)
        )
    )
    .pause(3)
    .exec(
      ws("Navigate to Reviews")
        .sendText(atmoMessage(createMessage("""{"type":"navigation","location":"","link":1}""")))
        .await(30 seconds)(
          ws.checkTextMessage("Sync and client id checks")
            .check(syncIdExtract, clientIdExtract)
        )
    )
    .pause(2)
    .exec(
      ws("Navigate back to Categories")
        .sendText(atmoMessage(createMessage("""{"type":"navigation","location":"categories","link":1}""")))
        .await(30 seconds)(
          ws.checkTextMessage("Categories view's checks")
            .check(gridIdExtract, searchFieldIdExtract, syncIdExtract, clientIdExtract)
        )
    )
    .pause(1)
    .exec(
      ws("Load view 2")
        .sendText(atmoMessage(createMessage("""{"type":"mSync","node":${searchFieldId},"feature":1,"property":"invalid","value":false},{"type":"publishedEventHandler","node":${gridId},"templateEventMethodName":"setDetailsVisible","templateEventMethodArgs":[null]},{"type":"publishedEventHandler","node":${gridId},"templateEventMethodName":"sortersChanged","templateEventMethodArgs":[[]]},{"type":"publishedEventHandler","node":${gridId},"templateEventMethodName":"confirmUpdate","templateEventMethodArgs":[0]}""")))
        .await(30 seconds)(
          ws.checkTextMessage("Sync and client id checks")
            .check(syncIdExtract, clientIdExtract)
        )
      )

    .pause(2)

  val scn = scenario("BevBuddyWS").repeat(1) {
    exec(chain_0)
  }

  setUp(scn.inject(rampUsers(1) during (1 seconds))).protocols(httpProtocol)
}
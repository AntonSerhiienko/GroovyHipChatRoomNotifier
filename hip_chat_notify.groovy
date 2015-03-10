//== HTTPBUILDER IMPORTS
@Grab(group = 'org.codehaus.groovy.modules.http-builder', module = 'http-builder', version = '0.7.2')
import groovyx.net.http.*
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*

//== END HTTPBUILDER IMPORTS

appUrl = "https://api.hipchat.com/v1/rooms/message"

def sendHipChatMessage(args) {
    def cli = new CliBuilder(usage: "groovy hip_chat_notify.groovy -from 'Autoconfig' -message 'Test failed' -color 'red' -room 'Automation team' -token '1e21112e46936d74c2468b81efe'")
    def http = new HTTPBuilder(appUrl)
    cli.f(longOpt: 'from', args: 1, 'Define to whom message will be send', required: true)
    cli.m(longOpt: 'message', args: 1, 'Define message to send', required: true)
    cli.c(longOpt: 'color', args: 1, "Define message color", required: false)
    cli.r(longOpt: 'room', args: 1, "Define room where to send message", required: true)
    cli.t(longOpt: 'token', args: 1, "Define token to be used", required: true)
    options = cli.parse(args)
    if (!options) {
        cli.usage
        return
    }

    http.request(GET) { req ->

        uri.query = [
                        auth_token: options.t,
                        format: 'json',
                        room_id: options.r,
                        from: options.f,
                        message: options.m,
                        color: options.c ? options.c : "green"
                    ]

        response.success = { resp, json ->
            println "Response status: ${resp.statusLine}"
            println "Response: ${json}"
        }
    }

}

sendHipChatMessage(args)
package com.ib.eventaid.common.data.api.utils

import androidx.test.platform.app.InstrumentationRegistry
import com.ib.eventaid.common.data.api.ApiConstants
import com.ib.logging.Logger
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import java.io.IOException
import java.io.InputStream

class FakeServer {
    private val mockWebServer = MockWebServer()

    private val endpointSeparator = "/"
    private val responsesBasePath = "networkresponses/"
    private val eventsEndpointPath = endpointSeparator + ApiConstants.EVENTS_ENDPOINT
    private val notFoundResponse = MockResponse().setResponseCode(404)

    val baseEndpoint
    get() = mockWebServer.url(endpointSeparator)

    fun start(){
        mockWebServer.start(8080)
    }

    fun setHappyPathDispatcher(){
        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                val path = request.path ?: return notFoundResponse

                return with(path){
                    when{
                        startsWith(eventsEndpointPath) -> {
                            MockResponse()
                                .setResponseCode(200)
                                .setBody(getJson("${responsesBasePath}events.json"))
                        }
                        else -> {notFoundResponse}
                    }
                }
            }
        }
    }

    fun shutDown(){
        mockWebServer.shutdown()
    }

    private fun getJson(path:String):String{
        return try {
            val context = InstrumentationRegistry.getInstrumentation().context
            val jsonStream: InputStream = context.assets.open(path)
            String(jsonStream.readBytes())
        }catch (exception:IOException){
            Logger.e(exception,"Error reading network response json asset")
            throw exception
        }
    }
}
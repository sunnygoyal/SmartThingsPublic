/**
 *  Virtual Curtain
 *
 *  Copyright 2021 Sunny Goyal
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 */
metadata {
	definition (name: "Virtual Curtain", namespace: "com.painless.virtual_curtain", author: "Painless", ocfDeviceType: "oic.d.blind", mnmn: "SmartThings", vid: "generic-shade-3") {
		capability "Window Shade"
        capability "Health Check"
	}

    // UI tile definitions
    tiles {
		multiAttributeTile(name:"windowShade", type: "generic", width: 6, height: 4) {
			tileAttribute("device.windowShade", key: "PRIMARY_CONTROL") {
				attributeState "open", label: 'Open', action: "close", icon: "http://www.ezex.co.kr/img/st/window_open.png", backgroundColor: "#00A0DC", nextState: "closed"
				attributeState "closed", label: 'Closed', action: "open", icon: "http://www.ezex.co.kr/img/st/window_close.png", backgroundColor: "#ffffff", nextState: "open"
			}
		}

        main "windowShade"
        details("windowShade")
    }
}

// parse events into attributes
def parse(String description) {
	log.debug "Parsing '${description}'"
}

def installed() {
    initialize()
}

def updated() {
    initialize()
}

def initialize() {
    sendEvent(name: "DeviceWatch-DeviceStatus", value: "online")
    sendEvent(name: "healthStatus", value: "online")
    sendEvent(name: "DeviceWatch-Enroll", value: [protocol: "cloud", scheme:"untracked"].encodeAsJson(), displayed: false)

	sendEvent(name: "windowShade", value: "closed", descriptionText: "$device.displayName is closed")
}


def open() {
	sendEvent(name: "windowShade", value: "open", descriptionText: "$device.displayName is closed")
}

def close() {
	sendEvent(name: "windowShade", value: "closed", descriptionText: "$device.displayName is closed")
}

def pause() {
	log.debug "Executing 'pause'"
}
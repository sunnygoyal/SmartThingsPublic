metadata {
    definition (name: "Painless Sensor", namespace: "com/painless", author: "Painless", ocfDeviceType: "x.com.st.d.sensor.contact") {
        capability "Sensor"
		capability "Contact Sensor"
        capability "Health Check"
        capability "Switch"
    }

    // UI tile definitions
    tiles {
    	multiAttributeTile(name: "contact", type: "generic", width: 6, height: 4) {
			tileAttribute("device.contact", key: "PRIMARY_CONTROL") {
				attributeState("open", label: '${name}', icon: "st.contact.contact.open", backgroundColor: "#e86d13")
				attributeState("closed", label: '${name}', icon: "st.contact.contact.closed", backgroundColor: "#00A0DC")
			}
		}

        standardTile("switch", "device.switch", width: 2, height: 2, canChangeIcon: true) {
            state "off", label: '${currentValue}', action: "switch.on",
                  icon: "st.switches.switch.off", backgroundColor: "#ffffff"
            state "on", label: '${currentValue}', action: "switch.off",
                  icon: "st.switches.switch.on", backgroundColor: "#00a0dc"
        }

        main "contact"
        details("contact", "switch")
    }
}

// Parse incoming device messages to generate events
def parse(String description) {
    def pair = description.split(":")
    createEvent(name: pair[0].trim(), value: pair[1].trim(), unit:"F")
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

    sendEvent(name: "switch", value: "off", isStateChange: true)
	sendEvent(name: "contact", value: "closed", descriptionText: "$device.displayName is closed")
}

def on() {
    sendEvent(name: "switch", value: "on", isStateChange: true)
    runIn(60, handleTimeout);
    log.debug "Timeout scheduled for 1 min"
}

def off() {
    sendEvent(name: "switch", value: "off", isStateChange: true)
	sendEvent(name: "contact", value: "closed", descriptionText: "$device.displayName is closed")
    unschedule(handleTimeout)
    log.debug "Cancelled scheduled"
}

def handleTimeout() {
	log.debug "Timeout occurred"
	if (device.currentState("switch")?.value == "on") {
		sendEvent(name: "contact", value: "open", descriptionText: "$device.displayName is open")
	} else {
		sendEvent(name: "contact", value: "closed", descriptionText: "$device.displayName is closed")
	}
	sendEvent(name: "contact", value: "open", descriptionText: "$device.displayName is open")
}

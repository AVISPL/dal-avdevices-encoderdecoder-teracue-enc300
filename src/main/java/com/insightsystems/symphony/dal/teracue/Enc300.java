package com.insightsystems.symphony.dal.teracue;

import com.avispl.symphony.api.dal.dto.monitor.ExtendedStatistics;
import com.avispl.symphony.api.dal.dto.monitor.Statistics;
import com.avispl.symphony.api.dal.dto.snmp.SnmpEntry;
import com.avispl.symphony.api.dal.monitor.Monitorable;
import com.avispl.symphony.dal.BaseDevice;


import java.util.*;

public class Enc300 extends BaseDevice implements Monitorable{
    private final List<String> snmpOids = new ArrayList<String>()
    {{
        add(".1.3.6.1.4.1.22145.3.3.4.1.1.0"); //Hostname
        add(".1.3.6.1.4.1.22145.3.3.3.1.7.0"); //EncoderRunning
        add(".1.3.6.1.4.1.22145.3.3.3.1.8.0"); //TotalBytesEncoded
        add(".1.3.6.1.4.1.22145.3.3.3.1.9.0"); //EncoderTransportRate
        add(".1.3.6.1.4.1.22145.3.3.3.2.8.0"); //VideoResolution
        add(".1.3.6.1.4.1.22145.3.3.3.2.13.0"); //VideoCodec
        add(".1.3.6.1.4.1.22145.3.3.3.6.6.0"); //RecordingActive
        add(".1.3.6.1.4.1.22145.3.3.3.6.7.0"); //RecordingMediaPresent
        add(".1.3.6.1.4.1.22145.3.3.5.2.1.0"); //SystemDate
        add(".1.3.6.1.4.1.22145.3.3.5.2.2.0"); //SystemTime
        add(".1.3.6.1.4.1.22145.3.3.5.1.1.0"); //Uptime
        add(".1.3.6.1.4.1.22145.3.3.5.1.2.0"); //SystemTemperature
        add(".1.3.6.1.4.1.22145.3.3.5.1.3.0"); //NetworkTx
        add(".1.3.6.1.4.1.22145.3.3.5.1.4.0"); //NetworkRx
    }};
    private final List<String> snmpKeys = new ArrayList<String>()
    {{
        add("Hostname");
        add("EncoderRunning");
        add("TotalBytesEncoded");
        add("EncoderTransportRate");
        add("VideoResolution");
        add("VideoCodec");
        add("RecordingActive");
        add("RecordingMediaPresent");
        add("SystemDate");
        add("SystemTime");
        add("Uptime");
        add("SystemTemperature");
        add("NetworkTx");
        add("NetworkRx");
    }};

    @Override
    public List<Statistics> getMultipleStatistics() throws Exception {
        ExtendedStatistics extStats = new ExtendedStatistics();
        Map<String,String> stats = new HashMap<>();

        Collection<SnmpEntry> snmpEntries = querySnmp(snmpOids);
        Iterator<SnmpEntry> si = snmpEntries.iterator();
        Iterator<String> ki = snmpKeys.iterator();

        while (ki.hasNext() && si.hasNext()){
            String key = ki.next();
            String value = si.next().getValue();
            if (!value.equals("Request timed out")) {
                switch(key){
                    case "TotalBytesEncoded":
                        stats.put(key,value + " Bytes");
                        break;
                    case "RecordingActive":
                    case "RecordingMediaPresent":
                    case "EncoderRunning":
                        stats.put(key, String.valueOf(value.equals("2")));
                        break;
                    case "NetworkTx":
                    case "NetworkRx":
                        stats.put(key, convertBps(value));
                        break;
                    case "SystemTemperature":
                        stats.put(key, (value.substring(0,value.length()-1) + "." + value.charAt(value.length()-1)).replace("/0+$/","") + "°C");
                        break;

                    default:
                        stats.put(key, value);
                }

            }
        }
        extStats.setStatistics(stats);
        return Collections.singletonList(extStats);
    }

    /**
     * Converts numeric string to formatted string in bit/s or kbit/s
     * @param value String value to be converted to bps string
     * @return Formatted string in bits or kbits per second
     */
    private String convertBps(String value) {
        try{
            final float bits = Float.parseFloat(value);
            if (bits >= 1000)
                return bits/1000 + " kbit/s";
            else
                return bits + " bit/s";
        } catch (Exception e){
            if (this.logger.isDebugEnabled())
                this.logger.debug("Unable to parse float in value: " + value + " Error: " + e.getMessage());
            return "";
        }
    }
}

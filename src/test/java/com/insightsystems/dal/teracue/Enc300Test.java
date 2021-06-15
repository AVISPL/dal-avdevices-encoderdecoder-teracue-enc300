package com.insightsystems.dal.teracue;

import com.avispl.symphony.api.dal.dto.monitor.ExtendedStatistics;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class Enc300Test {
    Enc300 device = new Enc300();

    @Before
    public void setup() throws Exception {
        device.setHost("192.168.215.78");
        device.setSnmpCommunity("public");
        device.init();
    }

    @Test
    public void checkAllExtendedProperties() throws Exception {
        Map<String,String> stats = ((ExtendedStatistics)device.getMultipleStatistics().get(0)).getStatistics();

        Assert.assertNotNull(stats.get("Hostname"));
        Assert.assertNotNull(stats.get("EncoderRunning"));
        Assert.assertNotNull(stats.get("TotalBytesEncoded"));
        Assert.assertNotNull(stats.get("EncoderTransportRate"));
        Assert.assertNotNull(stats.get("VideoResolution"));
        Assert.assertNotNull(stats.get("VideoCodec"));
        Assert.assertNotNull(stats.get("RecordingActive"));
        Assert.assertNotNull(stats.get("RecordingMediaPresent"));
        Assert.assertNotNull(stats.get("SystemDate"));
        Assert.assertNotNull(stats.get("SystemTime"));
        Assert.assertNotNull(stats.get("Uptime"));
        Assert.assertNotNull(stats.get("SystemTemperature"));
        Assert.assertNotNull(stats.get("NetworkTx"));
        Assert.assertNotNull(stats.get("NetworkRx"));
    }
}

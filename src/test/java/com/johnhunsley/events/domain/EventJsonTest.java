package com.johnhunsley.events.domain;

import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.directory.Directory;
import com.stormpath.sdk.organization.Organization;
import com.stormpath.sdk.organization.OrganizationList;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author John Hunsley
 *         jphunsley@gmail.com
 *         Date : 06/03/2017
 *         Time : 14:00
 */
@JsonTest
@RunWith(SpringJUnit4ClassRunner.class)
public class EventJsonTest {
    @Autowired
    private JacksonTester<Event> tester;

    @Test
    public void testSerialize() {
        Date now = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss:SSS");
        final String expected = "{\"class\":\"Event\",\"user\":\"12345abc\",\"created\":\""+df.format(now)+"\",\"longitude\":-50.09745,\"latitude\":1.0101,\"priority\":\"High\",\"hash\":\"KPZz8xzdavUNHwuOK3G55Q==\",\"org\":\"54321cba\"}";

        try {
            Hash hash = new Hash(Hash.MD5_TYPE);
            Event event = new Event("12345abc", now , Base64.getEncoder().encodeToString(hash.hash("121212121")), "54321cba");
            event.setLatitude(1.0101);
            event.setLongitude(-50.09745);
            event.setPriority("High");
            System.out.println(tester.write(event).getJson());
            assertThat(tester.write(event)).isEqualToJson(expected);

        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testDeserialize() {
        Date now = DateTime.now().toDate();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss:SSS");
        final String json = "{\"class\":\"Event\",\"user\":\"12345abc\",\"created\":\""+df.format(now)+"\",\"longitude\":-50.09745,\"latitude\":1.0101,\"priority\":\"High\",\"hash\":\"KPZz8xzdavUNHwuOK3G55Q==\",\"org\":\"54321cba\"}";

        try {
            Event event = tester.parseObject(json);
            assertTrue(event.getUser().equals("12345abc"));
            assertTrue(event.getOrg().equals("54321cba"));
            assertTrue(event.getHash().equals("KPZz8xzdavUNHwuOK3G55Q=="));

        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testDeserializeClassNameFailure() {
        Date now = DateTime.now().toDate();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss:SSS");
        final String json = "{\"class\":\"Teapot\",\"user\":\"12345abc\",\"created\":\""+df.format(now)+"\",\"longitude\":-50.09745,\"latitude\":1.0101,\"priority\":\"High\",\"hash\":\"KPZz8xzdavUNHwuOK3G55Q==\",\"org\":\"54321cba\"}";

        try {
            tester.parseObject(json);
            fail();

        } catch (IOException e) {
            //pass - cannot resolve class named 'Teapot'
        }
    }

}

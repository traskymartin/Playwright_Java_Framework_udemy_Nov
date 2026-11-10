package tests;

import java.util.HashMap;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.jayway.jsonpath.JsonPath;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;

public class APITest {

        @Test
        public void e2eAPITest() {

                // login API call
                HashMap<Object, Object> data = new HashMap<>();
                data.put("email", "trasky1508@gmail.com");
                data.put("password", "Master#91X");
                Playwright playwright = Playwright.create();
                APIRequestContext requestContext = playwright.request().newContext();
                APIResponse loginresponse = requestContext.post(
                                "https://api.eventhub.rahulshettyacademy.com/api/auth/login",
                                RequestOptions.create().setData(data));
                Assert.assertTrue(loginresponse.ok());

                String token = JsonPath.read(loginresponse.text(), "$.token");
                System.out.println(token);

                // create event API call
                HashMap<Object, Object> eventdata = new HashMap<>();
                eventdata.put("title", "Qa summit");
                eventdata.put("description", "Qa summit description");
                eventdata.put("category", "Conference");
                eventdata.put("venue", "I don't know");
                eventdata.put("city", "Bangalore");
                eventdata.put("eventDate", "2026-11-25T03:35:00.000Z");
                eventdata.put("price", 232);
                eventdata.put("totalSeats", 2323);

                APIResponse eventCreate = requestContext.post("https://api.eventhub.rahulshettyacademy.com/api/events",
                                RequestOptions.create().setHeader("Authorization", "Bearer " + token)
                                                .setData(eventdata));

                Assert.assertTrue(eventCreate.ok(), "Event creation successful");

                String eventId = JsonPath.read(eventCreate.text(), "$.data.id").toString();
                System.out.println("Event ID: " + eventId);

                // Get all events API call
                APIResponse allEventsResponse = requestContext
                                .get("https://api.eventhub.rahulshettyacademy.com/api/events",
                                                RequestOptions.create().setQueryParam("page", "1")
                                                                .setQueryParam("limit", "12")
                                                                .setHeader("Authorization", "Bearer " + token));

                Assert.assertTrue(allEventsResponse.ok(), "Get all events successful");
                System.out.println("All Events Response: " + allEventsResponse.text());

                List<Integer> allEventIds = JsonPath.read(allEventsResponse.text(), "$.data[*].id");
                System.out.println("All Event IDs: " + allEventIds);
                Assert.assertTrue(allEventIds.contains(Integer.parseInt(eventId)),
                                "Created event ID " + eventId + " found in the list of all events");

                // Delete event API call
                APIResponse deleteEventResponse = requestContext.delete(
                                "https://api.eventhub.rahulshettyacademy.com/api/events/" + eventId,
                                RequestOptions.create().setHeader("Authorization", "Bearer " + token));
                Assert.assertTrue(deleteEventResponse.ok(), "Event deletion successful");

                // Verify event deletion by fetching all events again
                 APIResponse afterDeleteEventsResponse = requestContext
                                .get("https://api.eventhub.rahulshettyacademy.com/api/events",
                                                RequestOptions.create().setQueryParam("page", "1")
                                                                .setQueryParam("limit", "12")
                                                                .setHeader("Authorization", "Bearer " + token));

                Assert.assertTrue(afterDeleteEventsResponse.ok(), "Get all events successful");
                System.out.println("All Events Response: " + afterDeleteEventsResponse.text());

                List<Integer> afterDeleteEventIds = JsonPath.read(afterDeleteEventsResponse.text(), "$.data[*].id");
                System.out.println("All Event IDs: " + afterDeleteEventIds);
                Assert.assertFalse(afterDeleteEventIds.contains(Integer.parseInt(eventId)),
                                "Created event ID " + eventId + " not found in the list of all events");
        }
}

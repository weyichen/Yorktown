package com.yorktown.yorktown;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Daniel on 1/22/2015.
 */
public class Step {
    private final String title;

    public String getTitle() {
        return title;
    }

    public String getDetails() {
        return details;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public String getLocationId() {
        return locationId;
    }

    private final String details;
    private final Date startTime;
    private final Date stopTime;
    private final String locationId;

    private Step(StepBuilder builder) {
        this.title = builder.title;
        this.details = builder.details;
        this.startTime = builder.startTime;
        this.stopTime = builder.stopTime;
        this.locationId = builder.locationId;
    }

    public static final class StepBuilder {
        private final String title;
        private String details;
        private Date startTime;
        private Date stopTime;
        private String locationId;

        public StepBuilder(String title) {
            this.title = title;
        }

        public StepBuilder withDetails(String details) {
            this.details = details;
            return this;
        }

        public StepBuilder withStartTime(String timeString) {
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss Z");
            try {
                this.startTime = format.parse(timeString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return this;
        }

        public StepBuilder withStopTime(String timeString) {
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss Z");
            try {
                this.startTime = format.parse(timeString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return this;
        }

        public StepBuilder withLocationId(String locationId) {
            this.locationId = locationId;
            return this;
        }

        public Step build() {
            return new Step(this);
        }
    }
}

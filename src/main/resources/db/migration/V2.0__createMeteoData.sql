CREATE SCHEMA IF NOT EXISTS stations;

CREATE TABLE IF NOT EXISTS stations."WeatherStation" (
                           id BIGSERIAL PRIMARY KEY,
                           "observationStationId" TEXT NOT NULL,
                           "name" VARCHAR(255) NULL,
                           "latitude" DOUBLE PRECISION NOT NULL,
                           "longitude" DOUBLE PRECISION NOT NULL,
                           "active" BOOLEAN NOT NULL DEFAULT TRUE NOT NULL,
                           "type" TEXT NOT NULL,
                           "createdAt" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
                           "modifiedAt" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
    );

DROP TYPE IF EXISTS station_type;
CREATE TYPE station_type AS ENUM ('WEATHER','RAIN','UNKNOWN');

ALTER TABLE stations."WeatherStation"
    ADD COLUMN IF NOT EXISTS "type" station_type;

CREATE TABLE IF NOT EXISTS stations."WeatherStationLinks" (
                            id BIGSERIAL PRIMARY KEY,
                            weather_station_id BIGSERIAL REFERENCES stations."WeatherStation" (id),
                            link TEXT
    );

CREATE TABLE IF NOT EXISTS stations."MeteoData" (
                            id BIGSERIAL PRIMARY KEY,
                            "stationId" bigint NOT NULL,
                            "measurementDate" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
                            "airTemperature" DOUBLE PRECISION,
                            "relativeHumidity" DOUBLE PRECISION,
                            "insolation" DOUBLE PRECISION,
                            "windSpeed" DOUBLE PRECISION,
                            "windDirection" DOUBLE PRECISION,
                            "airPressure" DOUBLE PRECISION,
                            "precipitation" DOUBLE PRECISION,
                            "dewPointTemperature" DOUBLE PRECISION,
                            "createdAt" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
                            "modifiedAt" TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
    );

CREATE TABLE IF NOT EXISTS stations."MeteoLinks" (
                           id BIGSERIAL PRIMARY KEY,
                           meteo_data_id BIGSERIAL REFERENCES stations."MeteoData" (id),
                           link TEXT
    );

ALTER TABLE stations."WeatherStationLinks" ADD CONSTRAINT "FK_WeatherStationLinks_WeatherStation_weather_station_id"
    FOREIGN KEY ("weather_station_id") REFERENCES stations."WeatherStation" (id) ON DELETE No Action ON UPDATE No Action;

ALTER TABLE stations."MeteoData"
    ADD CONSTRAINT "FK_MeteoData_WeatherStation"
        FOREIGN KEY ("stationId") REFERENCES stations."WeatherStation" (id) ON DELETE No Action ON UPDATE No Action;

ALTER TABLE stations."MeteoLinks" ADD CONSTRAINT "FK_MeteoLinks_MeteoData_meteo_data_id"
    FOREIGN KEY ("meteo_data_id") REFERENCES stations."MeteoLinks" (id) ON DELETE No Action ON UPDATE No Action;

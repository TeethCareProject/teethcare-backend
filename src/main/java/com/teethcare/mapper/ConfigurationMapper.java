package com.teethcare.mapper;

import org.mapstruct.MapperConfig;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.sql.Date;
import java.sql.Timestamp;

@MapperConfig(uses = ConfigurationMapping.class)
public interface ConfigurationMapper {

}

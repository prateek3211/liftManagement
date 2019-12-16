package com.fil.lift.config;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ConfigurationProperties
@Configuration
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnvironmentProperties {

	private Map<String, Map<String, List<String>>> liftGroup;
	private List<String> L1;
}

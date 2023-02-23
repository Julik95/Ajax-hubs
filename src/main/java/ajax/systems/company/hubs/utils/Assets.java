package ajax.systems.company.hubs.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Assets {
	
	REFRESH_WHITE_ICON("imgs/refresh-white.png"),
	ARM_WHITE_ICON("imgs/arm-white.png"),
	ARM_RED_ICON("imgs/arm-red.png"),
	ARM_LIGHT_RED_ICON("imgs/arm-light-red.png"),
	DISARM_WHITE_ICON("imgs/disarm-white.png"),
	DISARM_GREEN_ICON("imgs/disarm-green.png"),
	DISARM_LIGHT_GREEN_ICON("imgs/disarm-light-green.png"),
	ARM_NIGHT_VIOLET_ICON("imgs/arm-night-violet.png"),
	ARM_NIGHT_LIGHT_VIOLET_ICON("imgs/arm-night-light-violet.png"),
	ARM_NIGHT_WHITE_ICON("imgs/arm-night-white.png"),
	GROUPS_ORANGE_ICON("imgs/groups-orange.png"),
	GROUPS_LIGHT_ORANGE_ICON("imgs/groups-light-orange.png"),
	INFO_WHITE_ICON("imgs/info-white.png"),
	CHECK_BLUE_ICON("imgs/check-blue.png"),
	FEEDBACK_COLORED_ICON("imgs/feedback-colored.png");

	String path;

}

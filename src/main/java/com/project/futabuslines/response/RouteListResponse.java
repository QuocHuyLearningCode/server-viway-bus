package com.project.futabuslines.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RouteListResponse {
    private List<RouteResponse> routeResponses;
}
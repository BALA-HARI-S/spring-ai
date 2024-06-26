package ai.spring.demo_ai.entity;

import java.util.List;

public record Author(String name, List<String> books) {
}

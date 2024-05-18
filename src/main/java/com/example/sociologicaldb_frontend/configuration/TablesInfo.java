package com.example.sociologicaldb_frontend.configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class TablesInfo {
    public static final Map<String, List<String>> RESEARCH_ATTRIBUTES = Map.of(
            "Вильямс", List.of("Любознательность", "Воображение", "Сложность", "Склонность к рискy"),
            "Шварц", List.of("Безопасность", "Конформность", "Традиция", "Самостоятельность","Риск–новизна",
                    "Гедонизм", "Достижение", "Власть–богатство","Благожелательность","Универсализм"),
            "Медник", List.of("случайная;гора;долгожданная", "вечерняя;бумага;стенная", "обратно;родина;путь",
                    "далеко;слепой;будущее", "народная;страх;мировая", "деньги;билет;свободное", "человек;погоны;завод",
                    "дверь;доверие;быстро", "друг;город;круг", "поезд;купить;бумажный", "цвет;заяц;сахар",
                    "ласковая;морщины;сказка", "певец;Америка;тонкий", "тяжелый;рождение;урожайный", "много;чепуха;прямо",
                    "кривой;очки;острый", "садовая;мозг;пустая", "гость;случайно;вокзал"),
            "Триандис", List.of("фигуры, которые изображают ZUP"),
            "Социология", List.of("Друзья","Подписки","Подписки образование","Подписки опасные")
    ); //дополнить социологию

    public static List<String> getAttributesForResearch(String researchName) {
        return RESEARCH_ATTRIBUTES.getOrDefault(researchName, List.of());
    }

    public static List<String> getAllResearchNames() {
        return RESEARCH_ATTRIBUTES.keySet().stream().collect(Collectors.toList());
    }

    private TablesInfo(){}
}

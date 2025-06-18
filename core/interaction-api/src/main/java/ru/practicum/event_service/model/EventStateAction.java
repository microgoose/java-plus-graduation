package ru.practicum.event_service.model;

public enum EventStateAction {
    PUBLISH_EVENT,
    REJECT_EVENT,
    SEND_TO_REVIEW,
    CANCEL_REVIEW
}

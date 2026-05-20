create table analytics_event (
    id bigint not null auto_increment,
    visitor_id varchar(64) not null,
    event_type varchar(40) not null,
    path varchar(255),
    created_at datetime(6) not null,
    primary key (id)
);

create index idx_analytics_event_created_at on analytics_event (created_at);
create index idx_analytics_event_visitor_created on analytics_event (visitor_id, created_at);

create table movies_actors
(
  id bigint auto_increment,
  actor_id bigint,
  movie_id bigint,
  constraint pk_movies_actors primary key(id)
);

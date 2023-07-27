CREATE TABLE IF NOT EXISTS public.user_permission (
  id_user integer NOT NULL,
  id_permission integer NOT NULL,
  PRIMARY KEY (id_user, id_permission),
  FOREIGN KEY (id_user) REFERENCES users (id),
  FOREIGN KEY (id_permission) REFERENCES permission (id)
)

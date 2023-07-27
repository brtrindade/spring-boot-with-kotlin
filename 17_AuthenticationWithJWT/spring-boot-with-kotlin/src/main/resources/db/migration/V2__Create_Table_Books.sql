CREATE TABLE public.books (
  id serial PRIMARY KEY,
  author text NOT NULL,
  launch_date date,
  price decimal(65,2) NOT NULL,
  title text  NOT NULL
);

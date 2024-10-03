-- Initial data load for coach, hall, team, training_session, and team_training_session tables
-- Truncate tables
TRUNCATE TABLE public.coach, public.hall, public.team, public.training_session, public.team_training_session RESTART IDENTITY CASCADE;

-- Insert coaches
INSERT INTO public.coach ( email, name, phone, surname)
VALUES
    ( 'coach1@example.com', 'Coach', '0123456789', 'One'),
    ( 'coach2@example.com', 'Coach', '0123456789', 'Two'),
    ( 'coach3@example.com', 'Coach', '0123456789', 'Three'),
    ( 'coach4@example.com', 'Coach', '0123456789', 'Four'),
    ( 'coach5@example.com', 'Coach', '0123456789', 'Five'),
    ( 'coach6@example.com', 'Coach', '0123456789', 'Six'),
    ( 'coach7@example.com', 'Coach', '0123456789', 'Seven'),
    ( 'coach8@example.com', 'Coach', '0123456789', 'Eight');


-- Insert halles
INSERT INTO public.hall ( city, country, name, postal_code, street)
VALUES ( 'Strasbourg', 'France', 'Palais des Sports', '67000', 'Rue de lIll'),
       ( 'Paris', 'France', 'Stade Charléty', '75013', 'Avenue Pierre de Coubertin');



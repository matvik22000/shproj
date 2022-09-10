CREATE TABLE "teachers" (
                            "prs_id" int PRIMARY KEY,
                            "fio" text,
                            "another_info" text,
                            "is_admin" boolean
);

CREATE TABLE "classes" (
                           "class_number" varchar PRIMARY KEY,
                           "seats" int,
                           "responsible" int
);

CREATE TABLE "schedule" (
                            "reservation_id" SERIAL PRIMARY KEY,
                            "class_number" varchar,
                            "teacher_id" int,
                            "reason" text,
                            "start_time" timestamp,
                            "end_time" timestamp,
                            "customer_id" int
);

CREATE TABLE "class_types" (
                               "type_id" SERIAL PRIMARY KEY,
                               "type_description" text
);

CREATE TABLE "class2type" (
                              "class" varchar,
                              "type" int
);

ALTER TABLE "classes" ADD FOREIGN KEY ("responsible") REFERENCES "teachers" ("prs_id");

ALTER TABLE "schedule" ADD FOREIGN KEY ("class_number") REFERENCES "classes" ("class_number");

ALTER TABLE "schedule" ADD FOREIGN KEY ("teacher_id") REFERENCES "teachers" ("prs_id");

ALTER TABLE "schedule" ADD FOREIGN KEY ("customer_id") REFERENCES "teachers" ("prs_id");

ALTER TABLE "class2type" ADD FOREIGN KEY ("class") REFERENCES "classes" ("class_number");

ALTER TABLE "class2type" ADD FOREIGN KEY ("type") REFERENCES "class_types" ("type_id");

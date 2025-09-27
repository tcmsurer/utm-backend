CREATE TABLE hizmet (
                        id UUID PRIMARY KEY,
                        title VARCHAR(255) NOT NULL,
                        description TEXT,
                        video_url VARCHAR(255) NOT NULL,
                        created_date TIMESTAMP
);
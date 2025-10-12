
DROP TABLE IF EXISTS usta_rehberi;

CREATE TABLE rehber_icerik (
                               id UUID PRIMARY KEY,
                               title VARCHAR(255) NOT NULL,
                               description TEXT,
                               media_url TEXT NOT NULL,
                               media_type VARCHAR(50) NOT NULL, -- 'IMAGE' veya 'VIDEO'
                               is_active BOOLEAN DEFAULT TRUE NOT NULL,
                               created_date TIMESTAMP,
                               usta_id UUID NOT NULL,
                               CONSTRAINT fk_rehber_icerik_usta FOREIGN KEY(usta_id) REFERENCES usta(id)
);
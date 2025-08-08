CREATE TABLE IF NOT EXISTS admin_user (
                                          id UUID PRIMARY KEY,
                                          full_name VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(255),
    address VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS app_users (
                                         id UUID PRIMARY KEY,
                                         full_name VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(255),
    address VARCHAR(255),
    email_verified BOOLEAN NOT NULL DEFAULT false,
    email_verification_token VARCHAR(255),
    email_verification_token_expiry TIMESTAMP,
    password_reset_token VARCHAR(255),
    password_reset_token_expiry TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS usta (
                                    id UUID PRIMARY KEY,
                                    name VARCHAR(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS soru (
                                    id UUID PRIMARY KEY,
                                    question VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    soru_order INTEGER NOT NULL,
    usta_id UUID NOT NULL,
    CONSTRAINT fk_usta FOREIGN KEY(usta_id) REFERENCES usta(id)
    );

CREATE TABLE IF NOT EXISTS soru_options (
                                            soru_id UUID NOT NULL,
                                            options VARCHAR(255),
    CONSTRAINT fk_soru FOREIGN KEY(soru_id) REFERENCES soru(id)
    );

CREATE TABLE IF NOT EXISTS service_request (
                                               id UUID PRIMARY KEY,
                                               title VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(255),
    address TEXT,
    created_date TIMESTAMP,
    status VARCHAR(255) NOT NULL,
    user_id UUID NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY(user_id) REFERENCES app_users(id)
    );

CREATE TABLE IF NOT EXISTS request_details (
                                               service_request_id UUID NOT NULL,
                                               question VARCHAR(255) NOT NULL,
    answer VARCHAR(255),
    CONSTRAINT fk_service_request_details FOREIGN KEY(service_request_id) REFERENCES service_request(id)
    );

CREATE TABLE IF NOT EXISTS offer (
                                     id UUID PRIMARY KEY,
                                     price DOUBLE PRECISION NOT NULL,
                                     details TEXT,
                                     created_date TIMESTAMP,
                                     provider_id UUID,
                                     request_id UUID NOT NULL,
                                     CONSTRAINT fk_offer_provider FOREIGN KEY(provider_id) REFERENCES admin_user(id),
    CONSTRAINT fk_offer_request FOREIGN KEY(request_id) REFERENCES service_request(id)
    );

CREATE TABLE IF NOT EXISTS reply (
                                     id UUID PRIMARY KEY,
                                     sender_username VARCHAR(255) NOT NULL,
    text TEXT NOT NULL,
    reply_date TIMESTAMP,
    request_id UUID NOT NULL,
    CONSTRAINT fk_reply_request FOREIGN KEY(request_id) REFERENCES service_request(id)
    );

CREATE TABLE IF NOT EXISTS mail_log (
                                        id UUID PRIMARY KEY,
                                        email VARCHAR(255),
    subject VARCHAR(255),
    body TEXT,
    sent_date TIMESTAMP,
    request_id UUID,
    CONSTRAINT fk_mail_log_request FOREIGN KEY(request_id) REFERENCES service_request(id)
    );
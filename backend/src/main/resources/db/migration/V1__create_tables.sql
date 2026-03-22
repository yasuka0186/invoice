CREATE TABLE customers (
    customer_id UUID PRIMARY KEY,
    customer_code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    is_active BOOLEAN NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    version INTEGER NOT NULL
);

CREATE TABLE invoices (
    invoice_id UUID PRIMARY KEY,
    invoice_no VARCHAR(50) NOT NULL UNIQUE,
    customer_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    amount INTEGER NOT NULL,
    due_date DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    issued_at TIMESTAMPTZ NOT NULL,
    paid_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    version INTEGER NOT NULL,

    CONSTRAINT fk_invoices_customer
        FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

CREATE TABLE invoice_payments (
    payment_id UUID PRIMARY KEY,
    invoice_id UUID NOT NULL,
    paid_amount INTEGER NOT NULL,
    paid_at TIMESTAMPTZ NOT NULL,
    method VARCHAR(50) NOT NULL,
    note VARCHAR(500),
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    version INTEGER NOT NULL,

    CONSTRAINT fk_invoice_payments_invoice
        FOREIGN KEY (invoice_id) REFERENCES invoices(invoice_id)
);
ALTER TABLE pedidos_pagamento ALTER COLUMN nome_banco DROP NOT NULL;

ALTER TABLE pedidos_pagamento ALTER COLUMN numero_cartao DROP NOT NULL;

ALTER TABLE pedidos_pagamento ALTER COLUMN nome_no_cartao DROP NOT NULL;

ALTER TABLE pedidos_pagamento ALTER COLUMN validade_cartao DROP NOT NULL;

ALTER TABLE pedidos_pagamento ALTER COLUMN numero_de_parcelas DROP NOT NULL;

ALTER TABLE pedidos_pagamento ALTER COLUMN tipo_cartao DROP NOT NULL;
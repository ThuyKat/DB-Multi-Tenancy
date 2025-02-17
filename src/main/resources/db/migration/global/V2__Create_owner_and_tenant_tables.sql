-- table: owners
CREATE TABLE `global_multi_tenant`.`owners` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `status` ENUM('ACTIVE', 'DISABLE') NOT NULL DEFAULT 'ACTIVE',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE);
  
  -- table: tenants
  CREATE TABLE `global_multi_tenant`.`tenants` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `owner_id` BIGINT NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `db_name` VARCHAR(100) NOT NULL,
  `status` ENUM('ACTIVE', 'DISABLE') NOT NULL DEFAULT 'ACTIVE',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,
  UNIQUE INDEX `db_name_UNIQUE` (`db_name` ASC) VISIBLE,
  INDEX `fk_owner_id_idx` (`owner_id` ASC) VISIBLE,
  CONSTRAINT `fk_owner_id`
    FOREIGN KEY (`owner_id`)
    REFERENCES `global_multi_tenant`.`owners` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

   -- table: role - owner with default role for their tenants is SUPER_ADMIN
CREATE TABLE `global_multi_tenant`.`owner_role` (
  `owner_id` BIGINT NOT NULL,
  `tenant_id` BIGINT NOT NULL,
  `role` ENUM('SUPER_ADMIN', 'ADMIN') NOT NULL DEFAULT 'SUPER_ADMIN',
  PRIMARY KEY (`owner_id`, `tenant_id`),  -- Composite Primary Key
  INDEX `fk_owner_id_idx` (`owner_id` ASC) VISIBLE,
  INDEX `fk_tenant_id_idx` (`tenant_id` ASC) VISIBLE,
  CONSTRAINT `fk_role_owner_id`
    FOREIGN KEY (`owner_id`)
    REFERENCES `global_multi_tenant`.`owners` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_role_tenant_id`
    FOREIGN KEY (`tenant_id`)
    REFERENCES `global_multi_tenant`.`tenants` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

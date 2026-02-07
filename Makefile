DOCKER_COMPOSE = docker compose

BASE = -f docker-compose.base.yml

LOCAL_PROJECT   = sochief-local

LOCAL   = -p $(LOCAL_PROJECT)   $(BASE) -f docker-compose.local.yml   --env-file .env.local

.PHONY: \
	up-local deploy-local restart-local stop-local down-local logs-local ps-local rebuild-local clean-local \
	nuke-local

up-local:
	$(DOCKER_COMPOSE) $(LOCAL) up -d
deploy-local:
	$(DOCKER_COMPOSE) $(LOCAL) down
	$(DOCKER_COMPOSE) $(LOCAL) up -d --build
restart-local:
	$(DOCKER_COMPOSE) $(LOCAL) restart
stop-local:
	$(DOCKER_COMPOSE) $(LOCAL) stop
down-local:
	$(DOCKER_COMPOSE) $(LOCAL) down
logs-local:
	$(DOCKER_COMPOSE) $(LOCAL) logs -f
ps-local:
	$(DOCKER_COMPOSE) $(LOCAL) ps
rebuild-local:
	$(DOCKER_COMPOSE) $(LOCAL) build --no-cache

nuke-local:
	@echo "💣 NUKE LOCAL : reset complet + rebuild"
	$(DOCKER_COMPOSE) $(LOCAL) down -v --remove-orphans
	$(DOCKER_COMPOSE) $(LOCAL) build --no-cache
	$(DOCKER_COMPOSE) $(LOCAL) up -d
	@echo "🚀 LOCAL recréé proprement."

clean-local:
	@echo "Réinitialisation de la base locale..."
	$(DOCKER_COMPOSE) $(LOCAL) stop sochief-api || true
	$(DOCKER_COMPOSE) $(LOCAL) exec -T sochief-db dropdb -U $$POSTGRES_USER --if-exists $$DB_NAME_LOCAL
	$(DOCKER_COMPOSE) $(LOCAL) exec -T sochief-db createdb -U $$POSTGRES_USER $$DB_NAME_LOCAL
	$(DOCKER_COMPOSE) $(LOCAL) start sochief-api
	@echo "✅ Réinitialisation terminée."

COMPOSE_FILE=docker/docker-compose.yml
COMPOSE_CMD=docker-compose -f $(COMPOSE_FILE)
MVN_CMD=mvn clean install -DskipTests

# Targets affecting all containers
start:
	$(MVN_CMD); $(COMPOSE_CMD) up -d

stop:
	$(COMPOSE_CMD) down

rebuild:
	$(MVN_CMD); $(COMPOSE_CMD) up -d --build

clean:
	$(COMPOSE_CMD) down -v

logs:
	$(COMPOSE_CMD) logs -f

# Targets affecting only the main app, leaving other containers as is
start-llamabot:
	$(MVN_CMD); $(COMPOSE_CMD) up -d llamabot

stop-llamabot:
	$(COMPOSE_CMD) stop llamabot

rebuild-llamabot:
	$(MVN_CMD); $(COMPOSE_CMD) up -d --build llamabot

clean-llamabot:
	$(COMPOSE_CMD) rm -sfv llamabot

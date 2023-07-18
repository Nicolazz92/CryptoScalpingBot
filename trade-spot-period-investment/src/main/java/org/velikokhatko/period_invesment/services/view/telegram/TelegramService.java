package org.velikokhatko.period_invesment.services.view.telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.velikokhatko.period_invesment.services.trade.AbstractTradingService;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.velikokhatko.period_invesment.utils.Utils.truncate;

@Service
@Slf4j
@Profile("telegram")
public class TelegramService extends TelegramLongPollingBot {

    private AbstractTradingService abstractTradingService;
    private String token;
    private String name;
    private String chatId;

    @PostConstruct
    public void startBot() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            if (!update.getMessage().getChatId().equals(Long.valueOf(chatId))) {
                sendMessage(String.valueOf(update.getMessage().getChatId()), "Пшёл нах отседа");
                return;
            }
            sendMessage(chatId, "Баланс: " + abstractTradingService.countAllMoney() + '$');
        }
    }

    @Scheduled(cron = "5 * * * * *")
    public void checkHealth() {
        Duration duration = Duration.between(truncate(abstractTradingService.getHealthMonitor()), truncate(LocalDateTime.now()));
        if (duration.toMinutes() > 1) {
            sendMessage(chatId, String.format("Был пропущено %d минут работы торговой функции", duration.toMinutes()));
        }
    }

    public void sendMessage(String currentChatId, String text) {
        try {
            SendMessage message = new SendMessage();
            message.setChatId(currentChatId);
            message.setText(text);
            log.info(message.getChatId() + ": " + message.getText());
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    public void setAbstractTradingService(AbstractTradingService abstractTradingService) {
        this.abstractTradingService = abstractTradingService;
    }

    @Value("${telegram.bot.token}")
    public void setToken(String token) {
        this.token = token;
    }

    @Value("${telegram.bot.name}")
    public void setName(String name) {
        this.name = name;
    }

    @Value("${telegram.bot.chatId}")
    public void setChatId(String chatId) {
        this.chatId = chatId;
    }
}

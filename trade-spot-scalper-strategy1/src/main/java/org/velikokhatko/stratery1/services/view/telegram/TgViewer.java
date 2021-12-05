package org.velikokhatko.stratery1.services.view.telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.velikokhatko.stratery1.services.api.provider.AbstractBinanceApiProvider;
import org.velikokhatko.stratery1.services.trade.AbstractTradingService;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class TgViewer extends TelegramLongPollingBot {

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
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            String balance = String.valueOf(abstractTradingService.countAllMoney());
            SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
            message.setChatId(chatId);
            message.setText("Баланс: " + balance + '$');

            log.info(message.getChatId() + ": " + message.getText());

            try {
                execute(message); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
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

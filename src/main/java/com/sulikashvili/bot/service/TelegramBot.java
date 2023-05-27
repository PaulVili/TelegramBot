package com.sulikashvili.bot.service;

import com.sulikashvili.bot.config.BotConfig;
import com.sulikashvili.bot.entities.User;
import com.sulikashvili.bot.repositories.*;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;


@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig config;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ResidencesRepository residencesRepository;
    @Autowired
    private BuildingsRepository buildingsRepository;
    @Autowired
    private ManagementRepository managementRepository;
    @Autowired
    private DocumentsRepository documentsRepository;
    @Autowired
    private AddmissionContactsRepository addmissionContactsRepository;
    @Autowired
    private AddmissionWorkTimeRepository addmissionWorkTimeRepository;
    @Autowired
    private AddmissionHotlinePhoneRepository addmissionHotlinePhoneRepository;
    @Autowired
    private CallScheduleRepository callScheduleRepository;
    @Autowired
    private TeacherOfficeRepository teacherOfficeRepository;
    @Autowired
    private ScholarshipRepository scholarshipRepository;
    private static final String CONTACTS_TEXT = "Вы попали в раздел контактов" +
            "\n\nДля получения более подробных сведений используйте выпадающее меню";
    private static final String RECEIVING_DOCUMENTS = "Вы попали в раздел приема документов" +
            "\n\nДля получения более подробных сведений используйте выпадающее меню";
    private static final String ADMISSION_COMMITTEE = "Вы попали в раздел приемной комиссии" +
            "\n\nДля получения более подробных сведений используйте выпадающее меню";
    private static final String FIRST_COLLEGE_BUILDING_BUTTON = "FIRST_COLLEGE_BUILDING_BUTTON";
    private static final String SECOND_COLLEGE_BUILDING_BUTTON = "SECOND_COLLEGE_BUILDING_BUTTON";
    private static final String THIRD_COLLEGE_BUILDING_BUTTON = "THIRD_COLLEGE_BUILDING_BUTTON";
    private static final String KURCHATOV_COLLEGE_BUILDING_BUTTON = "KURCHATOV_COLLEGE_BUILDING_BUTTON";
    private static final String EKO_PARK_BUTTON = "EKO_PARK";
    private static final String FIRST_COLLEGE_RESIDENCES_BUTTON = "FIRST_COLLEGE_RESIDENCES_BUTTON";
    private static final String SECOND_COLLEGE_RESIDENCES_BUTTON = "SECOND_COLLEGE_RESIDENCES_BUTTON";
    private static final String GLAVDIR_BUTTON = "GLAVDIR";
    private static final String ZAM_DIR_UCHEB_RAB_BUTTON = "ZAM_DIR_UCHEB_RAB";
    private static final String ZAM_DIR_INNOV_TECHNOLOGY_BUTTON = "ZAM_DIR_INNOV_TECHNOLOGY";
    private static final String ZAM_DIR_VOSPIT_RAB_BUTTON = "ZAM_DIR_VOSPIT_RAB";
    private static final String ZAM_DIR_INFORM_TECHNOLOGY_BUTTON = "ZAM_DIR_INFORM_TECHNOLOGY";
    private static final String ZAM_DIR_INCLUSIVE_OBRAZ_BUTTON = "ZAM_DIR_INCLUSIVE_OBRAZ";
    private static final String ZAM_DIR_FIN_HOZ_BUTTON = "ZAM_DIR_FIN_HOZ";
    private static final String ZAM_DIR_SECURITY_BUTTON = "ZAM_DIR_SECURITY";
    private static final String FIRST_COUPLE_BUTTON = "FIRST_COUPLE_BUTTON";
    private static final String SECOND_COUPLE_BUTTON = "SECOND_COUPLE_BUTTON";
    private static final String THIRD_COUPLE_BUTTON = "THIRD_COUPLE_BUTTON";
    private static final String FOURTH_COUPLE_BUTTON = "FOURTH_COUPLE_BUTTON";
    private static final String FIFTH_COUPLE_BUTTON = "FIFTH_COUPLE_BUTTON";
    private static final String SIXTH_COUPLE_BUTTON = "SIXTH_COUPLE_BUTTON";
    private static final String HELP_TEXT = "Бот создан в качестве помощника для ориентирования студента в колледже." +
            "\n\n Для использования этого бота вводите нижеперечисленные команды: " +
            "\n\n Введите /start, чтобы увидеть приветствие." +
            "\n Введите /help, чтобы увидеть справочник." +
            "\n Введите /contacts, чтобы перейти в раздел контактов." +
            "\n Введите /receiving_documents, чтобы перейти в раздел приема документов" +
            "\n Введите /admission_committee, чтобы перейти в раздел приемной комиссии" +
            "\n Введите /schedule, чтобы перейти в раздел расписания звонков" +
            "\n Введите /get_teacher_office, чтобы узнать в каком кабинете можно найти преподователя" +
            "\n Введите /get_scholarship, чтобы узнать сумму стипендии";
    public TelegramBot(BotConfig config) {
        this.config = config;

        //Создаем менюшку с командами
        List <BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "Запуск бота"));
        listOfCommands.add(new BotCommand("/help", "Вызов справочника"));
        listOfCommands.add(new BotCommand("/contacts", "Контакты"));
        listOfCommands.add(new BotCommand("/receiving_documents", "Прием документов"));
        listOfCommands.add(new BotCommand("/admission_committee", "Приемная комиссия"));
        listOfCommands.add(new BotCommand("/schedule", "Расписание звонков"));
        listOfCommands.add(new BotCommand("/get_teacher_office", "Кабинет преподавателя"));
        listOfCommands.add(new BotCommand("/get_scholarship", "Стипендия"));

        //запускаем инициализацию введенных команд
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
    @Override
    public String getBotToken() {
        return config.getToken();
    }
    @Override
    public String getBotUsername() {
        return config.getBotName();
    }
    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){
            //Получаем обновления о полученных сообщениях
            String messageText = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            //switch для инициализации команд
                switch (messageText){
                    case "/start":
                        registerUser(update.getMessage());
                        startCommandReceived(chat_id, update.getMessage().getChat().getFirstName());
                        break;
                    case "Назад":
                        startCommandReceived(chat_id, update.getMessage().getChat().getFirstName());
                        break;
                    case "/help":
                        sendMessage(chat_id, HELP_TEXT);
                        break;
                    case "/contacts", "Контакты":
                        contactsMessage(chat_id, CONTACTS_TEXT);
                        break;
                    case "/receiving_documents", "Прием документов":
                        receiving_documents(chat_id, RECEIVING_DOCUMENTS);
                        break;
                    case "/admission_committee", "Приемная комиссия":
                        admission_committee(chat_id, ADMISSION_COMMITTEE);
                        break;
                    case "/schedule", "Расписание звонков":
                        callSchedule(chat_id);
                        break;
                    case "Документы":
                        getDocument(chat_id,1);
                        break;
                    case "Вступительные испытания":
                        getDocument(chat_id, 2);
                        break;
                    case "Зачисление":
                        getDocument(chat_id, 3);
                        break;
                    case "Руководство":
                        management(chat_id);
                        break;
                    case "Учебные корпуса":
                        collegeBuilding(chat_id);
                        break;
                    case "Общежития":
                        studentResidences(chat_id);
                        break;
                    case "Контакты комиссии":
                        getAddmissionContacts(chat_id, 1);
                        break;
                    case "Режим работы":
                        getAddmissionWorkTime(chat_id, 1);
                        break;
                    case "Телефон горячей линии":
                        getAddmissionHotlinePhone(chat_id, 1);
                        break;
                    case "/get_teacher_office", "Кабинеты преподавателей":
                        getTeacherName(chat_id);
                        break;
                    case "/get_scholarship", "Стипендия":
                        getScholarship(chat_id);
                        break;
                    default:
                        if (teacherOfficeRepository.findName(messageText).isEmpty()){
                            sendMessage(chat_id, "Извините, команда не распознана.");
                        }else {
                            getTeacherOffice(chat_id, messageText);
                        }
                        break;

                }
        }
        //Реакция на кнопки в сообщениях бота
        else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long message_id = update.getCallbackQuery().getMessage().getMessageId();
            long chat_id = update.getCallbackQuery().getMessage().getChatId();

            if (callbackData.equals(FIRST_COLLEGE_BUILDING_BUTTON)){
                getCollegeBuilding(chat_id, message_id, 1);
            }
            else if (callbackData.equals(SECOND_COLLEGE_BUILDING_BUTTON)) {
                getCollegeBuilding(chat_id, message_id, 2);
            }
            else if (callbackData.equals(THIRD_COLLEGE_BUILDING_BUTTON)) {
                getCollegeBuilding(chat_id, message_id, 3);
            }
            else if (callbackData.equals(KURCHATOV_COLLEGE_BUILDING_BUTTON)) {
                getCollegeBuilding(chat_id, message_id, 4);
            }
            else if (callbackData.equals(EKO_PARK_BUTTON)) {
                getCollegeBuilding(chat_id, message_id, 5);
            }

            else if (callbackData.equals(FIRST_COLLEGE_RESIDENCES_BUTTON)){
                getResidences(chat_id, message_id, 1);
            }
            else if (callbackData.equals(SECOND_COLLEGE_RESIDENCES_BUTTON)){
                getResidences(chat_id, message_id, 2);
            }

            else if (callbackData.equals(GLAVDIR_BUTTON)) {
                getManagement(chat_id, message_id, 1);
            }
            else if (callbackData.equals(ZAM_DIR_UCHEB_RAB_BUTTON)) {
                getManagement(chat_id, message_id, 2);
            }
            else if (callbackData.equals(ZAM_DIR_INNOV_TECHNOLOGY_BUTTON)) {
                getManagement(chat_id, message_id, 3);
            }
            else if (callbackData.equals(ZAM_DIR_VOSPIT_RAB_BUTTON)) {
                getManagement(chat_id, message_id, 4);
            }

            else if (callbackData.equals(ZAM_DIR_INFORM_TECHNOLOGY_BUTTON)){
                getManagement(chat_id, message_id, 5);
            }
            else if (callbackData.equals(ZAM_DIR_INCLUSIVE_OBRAZ_BUTTON)){
                getManagement(chat_id, message_id, 6);
            }
            else if (callbackData.equals(ZAM_DIR_FIN_HOZ_BUTTON)){
                getManagement(chat_id, message_id, 7);
            }
            else if (callbackData.equals(ZAM_DIR_SECURITY_BUTTON)){
                getManagement(chat_id, message_id, 8);
            }

            else if (callbackData.equals(FIRST_COUPLE_BUTTON)){
                getCallSchedule(chat_id, message_id, 1);
            }
            else if (callbackData.equals(SECOND_COUPLE_BUTTON)){
                getCallSchedule(chat_id, message_id, 2);
            }
            else if (callbackData.equals(THIRD_COUPLE_BUTTON)){
                getCallSchedule(chat_id, message_id, 3);
            }
            else if (callbackData.equals(FOURTH_COUPLE_BUTTON)){
                getCallSchedule(chat_id, message_id, 4);
            }
            else if (callbackData.equals(FIFTH_COUPLE_BUTTON)){
                getCallSchedule(chat_id, message_id, 5);
            }
            else if (callbackData.equals(SIXTH_COUPLE_BUTTON)){
                getCallSchedule(chat_id, message_id, 6);
            }
        }
    }
    //Получаем данные о стипендии из бд
    private void getScholarship(long chat_id){
        var getScholarshipText = scholarshipRepository.findAll();
        getScholarshipText.forEach(scholarship -> {
            String text = scholarship.getName() + " - " + scholarship.getSum() + "\n";
            sendMessage(chat_id, text);
        });
    }
    //Спрашиваем фамилию препода
    private void getTeacherName(long chat_id){
        SendMessage message = new SendMessage();
        message.setChatId(chat_id);
        message.setText("Для получении сведений в каком кабинете можно найти преподавателя введите фамилию:");
        executeMessage(message);
    }
    //Получаем данные об о кабинетах, за которые несет ответственность преподаватель из бд
    private void getTeacherOffice(long chat_id,String name){
       var getTeacherName =  teacherOfficeRepository.findName(name);
        getTeacherName.forEach(GetTeacherCabinet -> {
           String text = GetTeacherCabinet.getFirstName() + " " + GetTeacherCabinet.getSecondName() + " " + GetTeacherCabinet.getThirdName() + "\n" +
                   "Кабинет: " + GetTeacherCabinet.getCabinet();
           sendMessage(chat_id, text);
       });
    }

    //Получаем данные об общежитиях из бд
    private void getResidences(long chat_id, long message_id, Integer id){
        var residences = residencesRepository.findById(id);
        residences.ifPresent(ResidencesId -> {
            String text = ResidencesId.getNumber() +
                    "\n\nАдрес: " + ResidencesId.getAddress() +
                    "\nКонтактный телефон: " + ResidencesId.getPhone();
            editMessage(text, chat_id, message_id);
        });
    }
    //Получаем данные о корпусах из бд
    private void getCollegeBuilding(long chat_id, long message_id, Integer id){
        var buildings = buildingsRepository.findById(id);
        buildings.ifPresent(BuildingsId -> {
            String text;
            if (BuildingsId.getEmail() != null && !BuildingsId.getEmail().isEmpty() || BuildingsId.getFax() != null && !BuildingsId.getFax().isEmpty()){
                text = BuildingsId.getBuilding() +
                        "\n\nАдрес: " + BuildingsId.getAddress() +
                        "\nКонтактный телефон: " + BuildingsId.getPhone() +
                        "\nКонтактный факс: " + BuildingsId.getFax() +
                        "\nАдрес электронной почты: " + BuildingsId.getEmail();
            }else{
                text = BuildingsId.getBuilding() +
                        "\n\nАдрес: " + BuildingsId.getAddress() +
                        "\nКонтактный телефон: " + BuildingsId.getPhone();
            }
            editMessage(text, chat_id, message_id);
        });
    }
    //Получаем данные о руководстве из бд
    private void getManagement(long chat_id, long message_id, Integer id){
        var management = managementRepository.findById(id);
        management.ifPresent(managementId -> {
            String text = "Фамилия, имя, отчество: " + managementId.getName() +
                    "\nДолжность: " + managementId.getPost() +
                    "\nКонтактный телефон: " + managementId.getPhone() +
                    "\nАдрес электронной почты: " + managementId.getEmail();
            editMessage(text, chat_id, message_id);
        });
    }
    //Получаем данные о поступлении из бд
    private void getDocument(long chat_id, Integer id){
        var document = documentsRepository.findById(id);
        document.ifPresent(documentId -> {
            String text = documentId.getBody();
            sendMessage(chat_id, text);
        });
    }
    //Получаем данные о контактах приемной комиссии из бд
    private void getAddmissionContacts(long chat_id, Integer id){
        var addmission = addmissionContactsRepository.findById(id);
        addmission.ifPresent(documentId -> {
            String text = documentId.getContacts();
            sendMessage(chat_id, text);
        });
    }
    //Получаем данные о рабочем времени приемной комиссии из бд
    private void getAddmissionWorkTime(long chat_id, Integer id){
        var addmission = addmissionWorkTimeRepository.findById(id);
        addmission.ifPresent(documentId -> {
            String text = documentId.getWorkTime();
            sendMessage(chat_id, text);
        });
    }
    //Получаем данные о горячей линии приемной комиссии из бд
    private void getAddmissionHotlinePhone(long chat_id, Integer id){
        var addmission = addmissionHotlinePhoneRepository.findById(id);
        addmission.ifPresent(documentId -> {
            String text = documentId.getHotlinePhone();
            sendMessage(chat_id, text);
        });
    }
    //Получаем данные о расписании звонков из бд
    private void getCallSchedule(long chat_id, long message_id, Integer id){
        var call = callScheduleRepository.findById(id);
        call.ifPresent(callId -> {
            String text = callId.getName() +
                    "\nНачало: " + callId.getStartCalling() +
                    "\nКонец: " + callId.getEndCalling();
            editMessage(text, chat_id, message_id);
        });
    }
    

    //Спрашиваем у пользователя и создаем клавиатуру
    private void callSchedule(long chat_id){
        SendMessage message = new SendMessage();
        message.setChatId(chat_id);
        message.setText("Выберите пару");

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        var firstCouple = new InlineKeyboardButton();
        firstCouple.setText("1 пара");
        firstCouple.setCallbackData(FIRST_COUPLE_BUTTON);
        rowInline.add(firstCouple);
        rowsInLine.add(rowInline);

        rowInline = new ArrayList<>();
        var secondCouple = new InlineKeyboardButton();
        secondCouple.setText("2 пара");
        secondCouple.setCallbackData(SECOND_COUPLE_BUTTON);
        rowInline.add(secondCouple);
        rowsInLine.add(rowInline);

        rowInline = new ArrayList<>();
        var thirdCouple = new InlineKeyboardButton();
        thirdCouple.setText("3 пара");
        thirdCouple.setCallbackData(THIRD_COUPLE_BUTTON);
        rowInline.add(thirdCouple);
        rowsInLine.add(rowInline);

        rowInline = new ArrayList<>();
        var fourthCouple = new InlineKeyboardButton();
        fourthCouple.setText("4 пара");
        fourthCouple.setCallbackData(FOURTH_COUPLE_BUTTON);
        rowInline.add(fourthCouple);
        rowsInLine.add(rowInline);

        rowInline = new ArrayList<>();
        var fifthCouple = new InlineKeyboardButton();
        fifthCouple.setText("5 пара");
        fifthCouple.setCallbackData(FIFTH_COUPLE_BUTTON);
        rowInline.add(fifthCouple);
        rowsInLine.add(rowInline);

        rowInline = new ArrayList<>();
        var sixthCouple = new InlineKeyboardButton();
        sixthCouple.setText("6 пара");
        sixthCouple.setCallbackData(SIXTH_COUPLE_BUTTON);
        rowInline.add(sixthCouple);
        rowsInLine.add(rowInline);

        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);

        executeMessage(message);
    }

    //Спрашиваем у пользователя и создаем клавиатуру
    private void studentResidences(long chat_id) {
        SendMessage message = new SendMessage();
        message.setChatId(chat_id);
        message.setText("Выберите общежитие");

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        var firstCollegeResidences = new InlineKeyboardButton();
        firstCollegeResidences.setText("Общежитие №1");
        firstCollegeResidences.setCallbackData(FIRST_COLLEGE_RESIDENCES_BUTTON);

        var secondCollegeResidences = new InlineKeyboardButton();
        secondCollegeResidences.setText("Общежитие №2");
        secondCollegeResidences.setCallbackData(SECOND_COLLEGE_RESIDENCES_BUTTON);

        rowInline.add(firstCollegeResidences);
        rowInline.add(secondCollegeResidences);

        rowsInLine.add(rowInline);

        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);

        executeMessage(message);
    }
    //Спрашиваем у пользователя и создаем клавиатуру
    private void management(long chat_id) {
        SendMessage message = new SendMessage();
        message.setChatId(chat_id);
        message.setText("Выберите должность");

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        var glavDir = new InlineKeyboardButton();
        glavDir.setText("Директор");
        glavDir.setCallbackData(GLAVDIR_BUTTON);
        rowInline.add(glavDir);
        rowsInLine.add(rowInline);

        rowInline = new ArrayList<>();
        var zam_dir_ucheb_rab = new InlineKeyboardButton();
        zam_dir_ucheb_rab.setText("Зам дир по " +
                "\nучебной работе");
        zam_dir_ucheb_rab.setCallbackData(ZAM_DIR_UCHEB_RAB_BUTTON);
        rowInline.add(zam_dir_ucheb_rab);
        rowsInLine.add(rowInline);

        rowInline = new ArrayList<>();
        var zam_dir_innov_technology = new InlineKeyboardButton();
        zam_dir_innov_technology.setText("Зам дир по " +
                "\nинновационным технологиям");
        zam_dir_innov_technology.setCallbackData(ZAM_DIR_INNOV_TECHNOLOGY_BUTTON);
        rowInline.add(zam_dir_innov_technology);
        rowsInLine.add(rowInline);

        rowInline = new ArrayList<>();
        var zam_dir_vospit_rab = new InlineKeyboardButton();
        zam_dir_vospit_rab.setText("Зам дир по " +
                "\nвоспитательной работе");
        zam_dir_vospit_rab.setCallbackData(ZAM_DIR_VOSPIT_RAB_BUTTON);
        rowInline.add(zam_dir_vospit_rab);
        rowsInLine.add(rowInline);

        rowInline = new ArrayList<>();
        var zam_dir_inform_technology = new InlineKeyboardButton();
        zam_dir_inform_technology.setText("Зам дир по " +
                "\nинформационным технологиям");
        zam_dir_inform_technology.setCallbackData(ZAM_DIR_INFORM_TECHNOLOGY_BUTTON);
        rowInline.add(zam_dir_inform_technology);
        rowsInLine.add(rowInline);

        rowInline = new ArrayList<>();
        var zam_dir_inclusive_obraz = new InlineKeyboardButton();
        zam_dir_inclusive_obraz.setText("Зам дир по " +
                "\nинклюзивному образованию");
        zam_dir_inclusive_obraz.setCallbackData(ZAM_DIR_INCLUSIVE_OBRAZ_BUTTON);
        rowInline.add(zam_dir_inclusive_obraz);
        rowsInLine.add(rowInline);

        rowInline = new ArrayList<>();
        var zam_dir_fin_hoz = new InlineKeyboardButton();
        zam_dir_fin_hoz.setText("Зам дир по " +
                "\nфинансово-хозяйственной " +
                "\nдеятельности");
        zam_dir_fin_hoz.setCallbackData(ZAM_DIR_FIN_HOZ_BUTTON);
        rowInline.add(zam_dir_fin_hoz);
        rowsInLine.add(rowInline);

        rowInline = new ArrayList<>();
        var zam_dir_security = new InlineKeyboardButton();
        zam_dir_security.setText("Зам дир по безопасности");
        zam_dir_security.setCallbackData(ZAM_DIR_SECURITY_BUTTON);
        rowInline.add(zam_dir_security);
        rowsInLine.add(rowInline);

        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);

        executeMessage(message);
    }
    //Редактирование сообщений
    public void editMessage(String text, long chat_id, long message_id){
        EditMessageText message = new EditMessageText();
        message.setChatId(chat_id);
        message.setText(text);
        message.setMessageId((int) message_id);
        try {
            execute(message);
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
    //Спрашиваем у пользователя и создаем клавиатуру
    private void collegeBuilding(long chat_id) {
        SendMessage message = new SendMessage();
        message.setChatId(chat_id);
        message.setText("Выберите корпус");

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        var firstCollegeBuilding = new InlineKeyboardButton();
        firstCollegeBuilding.setText("Корпус №1");
        firstCollegeBuilding.setCallbackData(FIRST_COLLEGE_BUILDING_BUTTON);

        var secondCollegeBuilding = new InlineKeyboardButton();
        secondCollegeBuilding.setText("Корпус №2");
        secondCollegeBuilding.setCallbackData(SECOND_COLLEGE_BUILDING_BUTTON);

        rowInline.add(firstCollegeBuilding);
        rowInline.add(secondCollegeBuilding);

        rowsInLine.add(rowInline);

        rowInline = new ArrayList<>();

        var thirdCollegeBuilding = new InlineKeyboardButton();
        thirdCollegeBuilding.setText("Корпус №3");
        thirdCollegeBuilding.setCallbackData(THIRD_COLLEGE_BUILDING_BUTTON);

        var kurchatovCollegeBuilding = new InlineKeyboardButton();
        kurchatovCollegeBuilding.setText("Курчатов");
        kurchatovCollegeBuilding.setCallbackData(KURCHATOV_COLLEGE_BUILDING_BUTTON);

        rowInline.add(thirdCollegeBuilding);
        rowInline.add(kurchatovCollegeBuilding);

        rowsInLine.add(rowInline);

        rowInline = new ArrayList<>();

        var ekoPark = new InlineKeyboardButton();
        ekoPark.setText("ЭКО-ПАРК");
        ekoPark.setCallbackData(EKO_PARK_BUTTON);
        rowInline.add(ekoPark);

        rowsInLine.add(rowInline);

        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);

        executeMessage(message);
    }
    //Стартовая клавиатура
    private void startupMenu(long chat_id, String textToSend){
        List<KeyboardRow> Rows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Контакты");
        row.add("Прием документов");
        Rows.add(row);
        row = new KeyboardRow();
        row.add("Приемная комиссия");
        row.add("Расписание звонков");
        Rows.add(row);
        row = new KeyboardRow();
        row.add("Кабинеты преподавателей");
        row.add("Стипендия");
        Rows.add(row);
        //Вызов метода создания клавиатуры
        keyboard(chat_id, textToSend, Rows);
    }
    private void admission_committee(long chat_id, String textToSend) {
        List<KeyboardRow> Rows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Контакты комиссии");
        row.add("Режим работы");
        Rows.add(row);
        row = new KeyboardRow();
        row.add("Телефон горячей линии");
        row.add("Назад");
        Rows.add(row);
        //Вызов метода создания клавиатуры
        keyboard(chat_id, textToSend, Rows);
    }
    private void receiving_documents(long chat_id, String textToSend) {
        List<KeyboardRow> Rows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Документы");
        row.add("Вступительные испытания");
        Rows.add(row);
        row = new KeyboardRow();
        row.add("Зачисление");
        row.add("Назад");
        Rows.add(row);
        //Вызов метода создания клавиатуры
        keyboard(chat_id, textToSend, Rows);
    }
    private void contactsMessage(long chat_id, String textToSend) {
        //Создаем список клавиш клавиатуры
        List<KeyboardRow> Rows = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Руководство");
        row.add("Учебные корпуса");
        Rows.add(row);
        row = new KeyboardRow();
        row.add("Общежития");
        row.add("Назад");
        Rows.add(row);
        //Вызов метода создания клавиатуры
        keyboard(chat_id, textToSend, Rows);
    }
    //Регистрация пользователя в базе данных
    private void registerUser(Message msg) {
        if (userRepository.findById(msg.getChatId()).isEmpty()) {
            var chat_id = msg.getChatId();
            var chat = msg.getChat();

            //Создаем объект пользователя и присваиваем ему нужную нам информацию о пользователе
            User user = new User();
            user.setChat_id(chat_id);
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setUserName(chat.getUserName());

            //Сохраняем в бд
            userRepository.save(user);
        }
    }
    private void startCommandReceived(long chat_id, String name){
        //Добавляем к сообщению эмоджи
        String answer = EmojiParser.parseToUnicode("Привет, чем могу помочь, " + name + "?" + " :blush:" +
                "\n\n Для вызова справки введите команду /help");
        startupMenu(chat_id, answer);
    }
    private void keyboard(long chat_id, String textToSend, List<KeyboardRow> rows){
        // Инициализируем метод message из API телеграмма
        SendMessage message = new SendMessage();
        //Инициализируем входящие параметры
        message.setChatId(String.valueOf(chat_id));
        message.setText(textToSend);

        //Создаем клавиатуру
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        //Инициализируем клавиатуру
        keyboardMarkup.setKeyboard(rows);
        //Разрешаем клавиатуре изменяться
        keyboardMarkup.setOneTimeKeyboard(true);
        //Инициализируем клавиатуру
        message.setReplyMarkup(keyboardMarkup);
        //Запускаем метод телеграма
        executeMessage(message);
    }
    //Выполнение отправки сообщения
    private void executeMessage(SendMessage message){
        try {
            execute(message);
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
    private void sendMessage(long chat_id, String textToSend){
        // Инициализируем метод message из API телеграмма
        SendMessage message = new SendMessage();
        //Инициализируем входящие параметры
        message.setChatId(chat_id);
        message.setText(textToSend);
        //Выполнение отправки сообщения
        executeMessage(message);
    }

}

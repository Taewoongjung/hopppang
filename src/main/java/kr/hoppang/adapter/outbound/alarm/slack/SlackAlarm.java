package kr.hoppang.adapter.outbound.alarm.slack;

import static com.slack.api.model.block.Blocks.asBlocks;
import static com.slack.api.model.block.Blocks.divider;
import static com.slack.api.model.block.Blocks.header;
import static com.slack.api.model.block.Blocks.section;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.model.block.composition.TextObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import kr.hoppang.adapter.outbound.alarm.AlarmService;
import kr.hoppang.adapter.outbound.alarm.dto.ErrorAlarm;
import kr.hoppang.adapter.outbound.alarm.dto.NewEstimation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Async
@Slf4j
@Primary
@Component
public class SlackAlarm implements AlarmService {

    @Value(value = "${slack.token}")
    private String token;

    @Value(value = "${slack.channel.monitor.error}")
    private String errorAlarmChanel;

    @Value(value = "${slack.channel.monitor.new-estimation}")
    private String newEstimationChannel;

    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendErrorAlarm(final ErrorAlarm errorEvent) {

        try {
            List<TextObject> textObjects = new ArrayList<>();
            textObjects.add(markdownText(errorEvent.errorMsg()));

            MethodsClient methods = Slack.getInstance().methods(token);
            ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                    .channel(errorAlarmChanel)
                    .blocks(asBlocks(
                            header(header -> header.text(
                                    plainText("⚠️ " + errorEvent.methodName()))),
                            divider(),
                            section(section -> section.fields(textObjects)
                            ))).build();

            methods.chatPostMessage(request);

        } catch (SlackApiException | IOException e) {
            log.info("{} 채널에 슬랙 메시지 전송 실패", errorAlarmChanel);
        }
    }

    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendNewEstimation(final NewEstimation newEstimationEvent) {

        try {
            List<TextObject> textObjects = new ArrayList<>();

            if (newEstimationEvent.userName() != null) {
                textObjects.add(markdownText("- *고객명:* " + newEstimationEvent.userName() + "\n" +
                        "- *고객 주소:* " + newEstimationEvent.userAddress()));
                textObjects.add(markdownText("  \n"));

            } else {
                textObjects.add(markdownText("*(비로그인 유저)*\n"));
                textObjects.add(markdownText("  \n"));
            }

            newEstimationEvent.estimationList().forEach(e -> {
                textObjects.add(markdownText("*[" + e.chassisType().getChassisName() + "]*\n"
                        + "- *가로:* " + e.width() + "\n"
                        + "- *세로:* " + e.height()));
            });

            MethodsClient methods = Slack.getInstance().methods(token);
            ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                    .channel(newEstimationChannel)
                    .blocks(asBlocks(
                            header(header -> header.text(
                                    plainText("[견적 등록]"))),
                            divider(),
                            section(section -> section.fields(textObjects)
                            ))).build();

            methods.chatPostMessage(request);

        } catch (SlackApiException | IOException e) {
            log.info("{} 채널에 슬랙 메시지 전송 실패", newEstimationChannel);
        }
    }
}

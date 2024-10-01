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
import com.slack.api.model.block.LayoutBlock;
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
            List<LayoutBlock> blocks = new ArrayList<>();

            // Header block for title
            blocks.add(header(header -> header.text(plainText("[견적 등록]"))));
            blocks.add(divider()); // Divider for better visual separation

            // 고객명과 주소
            if (newEstimationEvent.userName() != null) {
                blocks.add(section(section -> section.text(markdownText(
                        "*:bust_in_silhouette: 고객명:* " + newEstimationEvent.userName()))));
                blocks.add(section(section -> section.text(markdownText(
                        "*:house_with_garden: 고객 주소:* " + newEstimationEvent.userAddress()))));
                blocks.add(divider()); // Add a divider after 유저 section
            } else {
                blocks.add(section(section -> section.text(markdownText(
                        "*:warning: 비로그인 유저*"))));
                blocks.add(divider()); // Add a divider after 비로그인 유저 section
            }

            // Estimation details
            blocks.add(section(section -> section.text(markdownText("*:office: 회사:* " + newEstimationEvent.company().getCompanyName()))));
            blocks.add(section(section -> section.text(markdownText("*:package: 견적 리스트:*"))));
            newEstimationEvent.estimationList().forEach(e -> {
                blocks.add(section(section -> section.text(markdownText(
                        "> *:window: 샤시 종류:* " + e.chassisType().getChassisName() + "\n" +
                                "> *:straight_ruler: 가로:* " + e.width() + " mm\n" +
                                "> *:straight_ruler: 세로:* " + e.height() + " mm\n"))));
            });

            blocks.add(divider()); // Final divider to end the message

            MethodsClient methods = Slack.getInstance().methods(token);
            ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                    .channel(newEstimationChannel)
                    .blocks(blocks)
                    .build();

            methods.chatPostMessage(request);

        } catch (SlackApiException | IOException e) {
            log.info("{} 채널에 슬랙 메시지 전송 실패", newEstimationChannel);
        }
    }
}

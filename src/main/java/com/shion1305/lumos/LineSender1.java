package com.shion1305.lumos;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.FlexComponent;
import com.linecorp.bot.model.message.flex.component.Image;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.component.box.BoxLinearGradient;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.unit.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class LineSender1 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        LineMessagingClient client = LineMessagingClient.builder(ConfigManager.getConfig("LineMessagingToken")).build();
        Bubble.BubbleBuilder bubbleBuilder = Bubble.builder();
        Box.BoxBuilder heroB = Box.builder();
        ArrayList<FlexComponent> heroComponents = new ArrayList<>();
        heroComponents.add(Text.builder().text("1/17").size("4xl").align(FlexAlign.END).color("#ffffff").build());
        heroComponents.add(Text.builder().text("のアクティビティ").offsetTop("8px").color("#ffffff").build());
        heroB.contents(heroComponents);
        heroB.justifyContent(FlexJustifyContent.CENTER).alignItems(FlexAlignItems.CENTER).background(BoxLinearGradient.builder().angle("30deg").startColor("#0261de").endColor("#0220de").build());
        Box.BoxBuilder bodyB = Box.builder();
        bodyB.layout(FlexLayout.VERTICAL);
        ArrayList<FlexComponent> bodyItems = new ArrayList<>();
        Box.BoxBuilder imageB = Box.builder().layout(FlexLayout.HORIZONTAL);
        ArrayList<FlexComponent> images = new ArrayList<>();
        images.add(Image.builder().url(URI.create("https://shion1305.com/Lumos/image/706873888461684866.png")).build());
        images.add(Image.builder().url(URI.create("https://shion1305.com/Lumos/image/706873888461684866.png")).build());
        images.add(Image.builder().url(URI.create("https://shion1305.com/Lumos/image/706873888461684866.png")).build());
        images.add(Image.builder().url(URI.create("https://shion1305.com/Lumos/image/706873888461684866.png")).build());
        imageB.contents(images)
                .width("100%")
                .alignItems(FlexAlignItems.CENTER)
                .spacing("10px")
                .paddingBottom("15px");
        bodyItems.add(imageB.build());

        Box.BoxBuilder nextPB = Box.builder().layout(FlexLayout.HORIZONTAL);
        ArrayList<FlexComponent> nextPBItem = new ArrayList<>();
        nextPBItem.add(Image.builder().url(URI.create("https://shion1305.com/Lumos/next1.png")).build());
        Box.BoxBuilder nextB = Box.builder().layout(FlexLayout.VERTICAL);
        ArrayList<FlexComponent> nextBItem = new ArrayList<>();
        nextBItem.add(Text.builder().text("次の活動日").align(FlexAlign.START).color("#ffffff").margin(FlexMarginSize.NONE).size("xxs").offsetTop("10px").offsetStart("20%").build());
        nextBItem.add(Text.builder().text("1/18").weight(Text.TextWeight.BOLD).size(FlexFontSize.XXXXL).color("#ffffff").align(FlexAlign.START).margin(FlexMarginSize.NONE).build());
        nextBItem.add(Text.builder().text("21:00~").size(FlexFontSize.XXL).color("#ffffff").align(FlexAlign.START).margin(FlexMarginSize.NONE).offsetBottom("10px").build());
        nextB.contents(nextBItem);
        nextPBItem.add(nextB.build());
        nextPB.contents(nextPBItem);
        nextPB.backgroundColor("#008fa1")
                .cornerRadius("10px")
                .background(BoxLinearGradient.builder().angle("145deg").startColor("#008fa1").endColor("#0af084").build())
                .margin(FlexMarginSize.NONE)
                .alignItems(FlexAlignItems.CENTER);
        bodyItems.add(nextPB.build());
        bodyItems.add(Image.builder().url(URI.create("https://158.101.84.209/Lumos/image/LumosLogo.png")).aspectMode(Image.ImageAspectMode.Fit)
                .margin(FlexMarginSize.NONE).size("60%").gravity(FlexGravity.TOP).aspectRatio(Image.ImageAspectRatio.R2TO1).build());
        bodyB.contents(bodyItems)
                .background(BoxLinearGradient.builder().angle("60deg")
                        .startColor("#282929")
                        .endColor("#4d4d4d")
                        .build())
                .paddingBottom(FlexPaddingSize.NONE);
        bubbleBuilder.hero(heroB.layout(FlexLayout.HORIZONTAL).build())
                .body(bodyB.build());
        System.out.println(bubbleBuilder.toString());
        PushMessage pushMessage = new PushMessage("U68c3c8e484974b3ca784315d1c2d23ec", FlexMessage.builder().contents(bubbleBuilder.build()).altText("Lumos").build());
        client.pushMessage(pushMessage).get();
    }
}

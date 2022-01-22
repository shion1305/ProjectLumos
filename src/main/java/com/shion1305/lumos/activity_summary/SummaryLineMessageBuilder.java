package com.shion1305.lumos.activity_summary;

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
import java.util.List;

public class SummaryLineMessageBuilder {
    private final String date;
    private final List<Long> members;
    private final String nextDate;
    private final String nextTime;

    public SummaryLineMessageBuilder(String date, List<Long> members, String nextDate, String nextTime) {
        this.date = date;
        if (members == null) this.members = new ArrayList<>();
        else this.members = members;
        this.nextDate = nextDate;
        this.nextTime = nextTime;
    }

    public SummaryLineMessageBuilder(String date, long member, String nextDate, String nextTime) {
        this.date = date;
        this.members = new ArrayList<>();
        this.members.add(member);
        this.nextDate = nextDate;
        this.nextTime = nextTime;
    }

    public void addMember(long member) {
        members.add(member);
    }

    public FlexMessage build() {
        Bubble.BubbleBuilder bubbleBuilder = Bubble.builder().size(Bubble.BubbleSize.KILO);
        Box.BoxBuilder heroB = Box.builder();
        ArrayList<FlexComponent> heroComponents = new ArrayList<>();
        heroComponents.add(Text.builder().text(date).size("4xl").align(FlexAlign.END).color("#ffffff").build());
        heroComponents.add(Text.builder().text("のアクティビティ").offsetTop("8px").color("#ffffff").build());
        heroB.contents(heroComponents);
        heroB.justifyContent(FlexJustifyContent.CENTER).alignItems(FlexAlignItems.CENTER).background(BoxLinearGradient.builder().angle("30deg").startColor("#0261de").endColor("#0220de").build());
        Box.BoxBuilder bodyB = Box.builder();
        bodyB.layout(FlexLayout.VERTICAL);
        ArrayList<FlexComponent> bodyItems = new ArrayList<>();
        if (members != null) {
            if (members.size() != 0) {
                //Number of icon image rows
                int numRow = members.size() / 4;
                if (members.size() % 4 != 0) numRow++;
                //icons will be distributed in n(n=2~3) or n+1
                //@base indicates the min number of icons in a row.
                int base = members.size() / numRow;
                //@rowIncrease represents number of rows with n+1 icons.
                int rowIncrease = members.size() - numRow * base;
                for (int i = 0, m = 0; i < numRow; i++) {
                    Box.BoxBuilder imageB = Box.builder().layout(FlexLayout.HORIZONTAL);
                    ArrayList<FlexComponent> images = new ArrayList<>();
                    for (int j = 0; j < base + (i < rowIncrease ? 1 : 0); j++) {
                        images.add(Image.builder().url(URI.create("https://shion1305.com/Lumos/image/" + String.valueOf(members.get(m++)) + ".png")).size("50px").build());
                    }
                    imageB.contents(images)
                            .alignItems(FlexAlignItems.CENTER)
                            .spacing("3px")
                            .paddingBottom("15px");
                    bodyItems.add(imageB.build());
                }
            }
        }
        Box.BoxBuilder nextPB = Box.builder().layout(FlexLayout.HORIZONTAL);
        ArrayList<FlexComponent> nextPBItem = new ArrayList<>();
        nextPBItem.add(Image.builder().url(URI.create("https://shion1305.com/Lumos/image/next.png")).flex(3).build());
        Box.BoxBuilder nextB = Box.builder().layout(FlexLayout.VERTICAL);
        ArrayList<FlexComponent> nextBItem = new ArrayList<>();
        nextBItem.add(Text.builder().text("次の活動日").align(FlexAlign.START).color("#ffffff").margin(FlexMarginSize.NONE).size("xxs").offsetTop("10px").offsetStart("20%").build());
        nextBItem.add(Text.builder().text(nextDate).weight(Text.TextWeight.BOLD).size(FlexFontSize.XXXXL).color("#ffffff").align(FlexAlign.START).margin(FlexMarginSize.NONE).build());
        nextBItem.add(Text.builder().text(nextTime).size(FlexFontSize.XXL).color("#ffffff").align(FlexAlign.START).margin(FlexMarginSize.NONE).offsetBottom("10px").build());
        nextB.contents(nextBItem);
        nextPBItem.add(nextB.flex(5).build());
        nextPB.contents(nextPBItem);
        nextPB.backgroundColor("#008fa1")
                .cornerRadius("10px")
                .background(BoxLinearGradient.builder().angle("145deg").startColor("#008fa1").endColor("#0af084").build())
                .margin(FlexMarginSize.NONE)
                .alignItems(FlexAlignItems.CENTER);
        bodyItems.add(nextPB.build());
        bodyItems.add(Image.builder().url(URI.create("https://shion1305.com/Lumos/image/LumosLogo.png")).aspectMode(Image.ImageAspectMode.Fit)
                .margin(FlexMarginSize.NONE).size("60%").gravity(FlexGravity.TOP).aspectRatio(Image.ImageAspectRatio.R2TO1).build());
        bodyB.contents(bodyItems)
                .background(BoxLinearGradient.builder().angle("60deg")
                        .startColor("#282929")
                        .endColor("#4d4d4d")
                        .build())
                .paddingBottom(FlexPaddingSize.NONE);
        bubbleBuilder.hero(heroB.layout(FlexLayout.HORIZONTAL).build())
                .body(bodyB.build());
        return FlexMessage.builder().contents(bubbleBuilder.build()).altText("Lumos").build();
    }
}

package org.zycong.theHordes.helpers.PlaceHolder.PlaceholderTypes;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.zycong.theHordes.helpers.PlaceHolder.PlaceholderUtils.Placeholder;

public class ChatPlaceholders {

  @Placeholder(name = "messageChat")
  public static String messageChat(AsyncChatEvent e) {
    String output = PlainTextComponentSerializer.plainText().serialize(e.message());
    return output;
  }

}

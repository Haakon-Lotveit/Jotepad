package no.haakon.jotepad.experimental;

import gnu.trove.list.array.TCharArrayList;

public class TextContent implements CharSequence {
    TCharArrayList characters = new TCharArrayList(4096);
    int pointer = 0;

    public TextContent addContent(String content) {
        addContent(content.toCharArray());
        return this;
    }

    public TextContent addContent(char[] content) {
        characters.addAll(content);
        return this;
    }

    public TextContent addContent(CharSequence content) {
        for(int i = 0; i < content.length(); ++i) {
            characters.add(content.charAt(i));
        }
        return this;
    }


    @Override
    public int length() {
        return pointer;
    }

    @Override
    public char charAt(int index) {
        return characters.getQuick(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        TextContent subseq = new TextContent();
        subseq.characters = new TCharArrayList(this.characters.subList(start, end));
        return subseq;
    }

    @Override
    public String toString() {
        return characters.toString();
    }
}

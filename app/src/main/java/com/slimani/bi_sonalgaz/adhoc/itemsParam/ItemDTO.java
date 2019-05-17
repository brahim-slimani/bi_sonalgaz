package com.slimani.bi_sonalgaz.adhoc.itemsParam;

public class ItemDTO {

    private boolean checked = false;

    private String text = "";

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

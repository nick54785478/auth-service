package com.example.demo.domain.function.aggregate.vo;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ActionType {

	B("BROWSE", "B"), // 瀏覽（Browse， 回傳多筆資料的 READ)
	R("READ", "R"), // 讀取（Read）
	E("EDIT", "E"), // 編輯（Edit）
	A("ADD", "A"), // 建立（Add）
	D("DELETE", "D"), // 刪除（Delete）
	S("SEARCH", "S"), // 搜尋（Search）
	ALL("ALL", "ALL"); // 所有權限

	@Getter
	private final String code;
	@Getter
	private final String label;

	// enum 轉 Map
	private static final Map<String, ActionType> labelToTypeMap = new HashMap<>();

	static {
		for (ActionType type : ActionType.values()) {
			labelToTypeMap.put(type.label, type);
		}
	}

	public static ActionType fromLabel(String label) {
		return labelToTypeMap.get(label);
	}

	public static Boolean checkKind(String label) {
		ActionType kind = ActionType.fromLabel(label);
		if (kind == null) {
			return false;
		}
		return true;
	}

}

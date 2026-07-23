interface SearchBarProps {
  value: string;
  onChange: (value: string) => void;
  disabled: boolean;
}

export default function SearchBar({
  value,
  onChange,
  disabled,
}: SearchBarProps) {
  return (
    <input
      placeholder="Search news..."
      value={value}
      onChange={(e) => onChange(e.target.value)}
      style={{
        padding: "6px 8px",
        borderRadius: 8,
        border: "1px solid #ccc",
        minWidth: 220,
      }}
      disabled={disabled}
    />
  );
}
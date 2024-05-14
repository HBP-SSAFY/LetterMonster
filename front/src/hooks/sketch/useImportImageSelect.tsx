import { useCallback } from "react";

function useImportImageSelect(
  SIZE: number,
  setImage: React.Dispatch<React.SetStateAction<HTMLImageElement | undefined>>
) {
  const onImportImageSelect: React.ChangeEventHandler<HTMLInputElement> =
    useCallback(
      (e) => {
        if (e.target.files?.[0]) {
          const imageUrl = URL.createObjectURL(e.target.files[0]);
          const imageImport = new Image();
          imageImport.src = imageUrl;
          imageImport.onload = () => {
            setImage(imageImport);
          };
        }
        e.target.value = "";
      },
      [SIZE, setImage]
    );

  return { onImportImageSelect };
}

export default useImportImageSelect;
